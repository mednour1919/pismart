<?php

namespace App\Controller;

use App\Entity\DepenseProjet;
use App\Form\DepenseProjetType;
use App\Repository\DepenseProjetRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/depense/projet')]
final class DepenseProjetController extends AbstractController
{
    #[Route('/', name: 'app_depense_projet_index', methods: ['GET'])]
    public function index(DepenseProjetRepository $depenseProjetRepository): Response
    {
        return $this->render('depense_projet/index.html.twig', [
            'depense_projets' => $depenseProjetRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_depense_projet_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $depenseProjet = new DepenseProjet();
        $form = $this->createForm(DepenseProjetType::class, $depenseProjet);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($depenseProjet);
            $entityManager->flush();
    
            return $this->redirectToRoute('app_depense_projet_index', [], Response::HTTP_SEE_OTHER);
        }
    
        return $this->render('depense_projet/new.html.twig', [
            'depense_projet' => $depenseProjet,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_depense_projet_show', methods: ['GET'])]
    public function show(DepenseProjet $depenseProjet): Response
    {
        return $this->render('depense_projet/show.html.twig', [
            'depense_projet' => $depenseProjet,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_depense_projet_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, DepenseProjet $depenseProjet, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(DepenseProjetType::class, $depenseProjet);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_depense_projet_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('depense_projet/edit.html.twig', [
            'depense_projet' => $depenseProjet,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_depense_projet_delete', methods: ['POST'])]
    public function delete(Request $request, DepenseProjet $depenseProjet, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$depenseProjet->getId(), $request->getPayload()->get('_token'))) {
            $entityManager->remove($depenseProjet);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_depense_projet_index', [], Response::HTTP_SEE_OTHER);
    }
}