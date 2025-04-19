<?php

namespace App\Controller;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Authentication\Token\Storage\TokenStorageInterface;

class LoginController extends AbstractController
{
    #[Route('/login', name: 'app_login', methods: ['GET', 'POST'])]
    public function login(Request $request, EntityManagerInterface $em, SessionInterface $session, TokenStorageInterface $tokenStorage): Response
    {
        if ($request->isMethod('POST')) {
            $username = $request->request->get('username');
            $password = $request->request->get('password');

            // Find the user by username
            $user = $em->getRepository(User::class)->findOneBy(['username' => $username]);

            if ($user && $user->getPassword() === $password) {
                // Create the token and store it in the token storage
                $token = new UsernamePasswordToken($user, 'main', $user->getRoles());
                $tokenStorage->setToken($token);

                // Store user ID in the session
                $session->set('user_id', $user->getId());

                // Redirect to the dashboard
                return $this->redirectToRoute('app_dashboard');
            }

            // If login fails, display an error
            $error = 'Invalid username or password';
            return $this->render('User/login.html.twig', [
                'error' => $error,
            ]);
        }

        // If it's a GET request, show the login form
        return $this->render('User/login.html.twig');
    }

    #[Route('/logout', name: 'app_logout')]
    public function logout(SessionInterface $session): Response
    {
        // Clear user ID from the session
        $session->remove('user_id');

        // Redirect to the login page
        return $this->redirectToRoute('app_login');
    }
}
