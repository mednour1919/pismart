<?php

namespace App\Controller;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/register')]
final class RegistrationController extends AbstractController
{
    #[Route(name: 'app_register', methods: ['GET', 'POST'])]
    public function register(Request $request, EntityManagerInterface $em): Response
    {
        $error = null;

        if ($request->isMethod('POST')) {
            $username = $request->request->get('username');
            $email = $request->request->get('email');
            $password = $request->request->get('password');

            // Validate username
            if (!preg_match('/^[a-zA-Z0-9]{5,}$/', $username)) {
                $error = "Le nom d'utilisateur doit être alphabétique/numérique et contenir au moins 5 caractères.";

            // Validate password
            } elseif (strlen($password) < 5) {
                $error = "Le mot de passe doit contenir au moins 5 caractères.";

            // Check if username exists
            } elseif ($em->getRepository(User::class)->findOneBy(['username' => $username])) {
                $error = "Ce nom d'utilisateur est déjà pris.";

            // Check if email exists
            } elseif ($em->getRepository(User::class)->findOneBy(['email' => $email])) {
                $error = "Cet email est déjà utilisé.";

            } else {
                // All checks passed, create the user
                $user = new User();
                $user->setUsername($username);
                $user->setEmail($email);
                $user->setPassword($password); // No hashing

                $em->persist($user);
                $em->flush();

                return $this->redirectToRoute('app_login');
            }
        }

        return $this->render('User/register.html.twig', [
            'error' => $error,
        ]);
    }
}
