<?php

namespace App\Controller;

use App\Entity\ZoneDeCollecte;
use App\Form\ZoneDeCollecteType;
use App\Repository\ZoneDeCollecteRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Serializer\SerializerInterface;
use League\Csv\Writer;
use League\Csv\Reader;

#[Route('/zonecl')]
final class ZoneDeCollecteCrudController extends AbstractController
{
    #[Route(name: 'app_zone_de_collecte_crud_index', methods: ['GET'])]
    public function index(ZoneDeCollecteRepository $zoneDeCollecteRepository): Response
    {
        return $this->render('zone_de_collecte_crud/index.html.twig', [
            'zone_de_collectes' => $zoneDeCollecteRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_zone_de_collecte_crud_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $zoneDeCollecte = new ZoneDeCollecte();
        $form = $this->createForm(ZoneDeCollecteType::class, $zoneDeCollecte);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($zoneDeCollecte);
            $entityManager->flush();

            return $this->redirectToRoute('app_zone_de_collecte_crud_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('zone_de_collecte_crud/new.html.twig', [
            'zone_de_collecte' => $zoneDeCollecte,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_zone_de_collecte_crud_show', methods: ['GET'])]
    public function show(ZoneDeCollecte $zoneDeCollecte): Response
    {
        return $this->render('zone_de_collecte_crud/show.html.twig', [
            'zone_de_collecte' => $zoneDeCollecte,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_zone_de_collecte_crud_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, ZoneDeCollecte $zoneDeCollecte, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(ZoneDeCollecteType::class, $zoneDeCollecte);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_zone_de_collecte_crud_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('zone_de_collecte_crud/edit.html.twig', [
            'zone_de_collecte' => $zoneDeCollecte,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_zone_de_collecte_crud_delete', methods: ['POST'])]
    public function delete(Request $request, ZoneDeCollecte $zoneDeCollecte, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$zoneDeCollecte->getId(), $request->getPayload()->get('_token'))) {
            $entityManager->remove($zoneDeCollecte);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_zone_de_collecte_crud_index', [], Response::HTTP_SEE_OTHER);
    }

    
}