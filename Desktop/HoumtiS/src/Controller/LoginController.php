<?php

namespace App\Controller;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/login')]
class LoginController extends AbstractController
{
    #[Route(name: 'app_login', methods: ['GET', 'POST'])]
    public function login(Request $request, EntityManagerInterface $em, SessionInterface $session): Response
    {
        // Handle form submission
        if ($request->isMethod('POST')) {
            $username = $request->request->get('username');
            $password = $request->request->get('password');
            
            // Find user by username
            $user = $em->getRepository(User::class)->findOneBy(['username' => $username]);
            
            // If user exists and password matches
            if ($user && $user->getPassword() === $password) {
                // Store user ID in session
                $session->set('user_id', $user->getId());
                
                // Redirect to dashboard
                return $this->redirectToRoute('app_dashboard');
            }
            
            // Login failed
            $error = 'Invalid username or password';
            
            return $this->render('User/login.html.twig', [
                'error' => $error,
            ]);
        }
        
        // Display login form
        return $this->render('User/login.html.twig');
    }
    
    #[Route('/logout', name: 'app_logout')]
    public function logout(SessionInterface $session): Response
    {
        // Remove user ID from session
        $session->remove('user_id');
        
        // Redirect to login page
        return $this->redirectToRoute('app_login');
    }
}