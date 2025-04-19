<?php

namespace App\Controller;

use App\Entity\Contribution;
use App\Form\ContributionType;
use App\Repository\ContributionRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Security\Http\Attribute\IsGranted;

#[Route('/contribution')]
class ContributionController extends AbstractController
{
    #[Route('/', name: 'app_contribution_index', methods: ['GET'])]
    public function index(ContributionRepository $contributionRepository): Response
    {
        return $this->render('contribution/index.html.twig', [
            'contributions' => $contributionRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_contribution_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $contribution = new Contribution();
        $form = $this->createForm(ContributionType::class, $contribution);
        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                // Définir la date de création si non renseignée
                if (!$contribution->getDateCreation()) {
                    $contribution->setDateCreation(new \DateTime());
                }

                // Définir le statut par défaut si non renseigné
                if (!$contribution->getStatut()) {
                    $contribution->setStatut('en_attente');
                }

                $entityManager->persist($contribution);
                $entityManager->flush();

                $this->addFlash('success', 'Contribution créée avec succès');
                return $this->redirectToRoute('app_contribution_index');
            } else {
                $this->addFlash('error', 'Erreur dans le formulaire');
            }
        }

        return $this->render('contribution/new.html.twig', [
            'contribution' => $contribution,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_contribution_show', methods: ['GET'])]
    public function show(Contribution $contribution): Response
    {
        return $this->render('contribution/show.html.twig', [
            'contribution' => $contribution,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_contribution_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Contribution $contribution, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ContributionType::class, $contribution);
        $form->handleRequest($request);

        if ($form->isSubmitted()) {
            if ($form->isValid()) {
                $entityManager->flush();

                $this->addFlash('success', 'Contribution mise à jour avec succès');
                return $this->redirectToRoute('app_contribution_index');
            } else {
                $this->addFlash('error', 'Erreur dans le formulaire');
            }
        }

        return $this->render('contribution/edit.html.twig', [
            'contribution' => $contribution,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_contribution_delete', methods: ['POST'])]
    public function delete(Request $request, Contribution $contribution, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$contribution->getId(), $request->getPayload()->get('_token'))) {
            $entityManager->remove($contribution);
            $entityManager->flush();
            $this->addFlash('success', 'Contribution supprimée avec succès');
        } else {
            $this->addFlash('error', 'Token CSRF invalide');
        }

        return $this->redirectToRoute('app_contribution_index');
    }
}