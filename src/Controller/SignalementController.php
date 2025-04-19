<?php

namespace App\Controller;

use App\Entity\Signalement;
use App\Form\SignalementType;
use App\Repository\SignalementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/signalement')]
final class SignalementController extends AbstractController
{
    #[Route(name: 'app_signalement_index', methods: ['GET'])]
    public function index(SignalementRepository $signalementRepository): Response
    {
        return $this->render('signalement/index.html.twig', [
            'signalements' => $signalementRepository->findAll(),
        ]);
    }
    #[Route('/signalement/image/{id}', name: 'signalement_image', methods: ['GET'])]
    public function image(int $id, SignalementRepository $signalementRepository): Response
    {
    $signalement = $signalementRepository->find($id);

    if (!$signalement || !$signalement->getImage()) {
        throw $this->createNotFoundException('Image non trouvée');
    }

    return new Response(
        stream_get_contents($signalement->getImage()),
        200,
        [
            'Content-Type' => 'image/jpeg', // change si PNG ou autre
            'Content-Disposition' => 'inline'
        ]
    );
    }


    #[Route('/new', name: 'app_signalement_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $signalement = new Signalement();
        $form = $this->createForm(SignalementType::class, $signalement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            /** @var UploadedFile $imageFile */
            $imageFile = $form->get('image')->getData();
            
            if ($imageFile) {
                try {
                    $imageContent = file_get_contents($imageFile->getPathname());
                    $signalement->setImage($imageContent);
                } catch (\Exception $e) {
                    $this->addFlash('error', 'Erreur lors du traitement de l\'image');
                    return $this->redirectToRoute('app_signalement_new');
                }
            }

            $entityManager->persist($signalement);
            $entityManager->flush();

            $this->addFlash('success', 'Signalement créé avec succès!');
            return $this->redirectToRoute('app_signalement_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('signalement/new.html.twig', [
            'signalement' => $signalement,
            'form' => $form,
        ]);
    }

    #[Route('/{id_signalement}', name: 'app_signalement_show', methods: ['GET'])]
    public function show(Signalement $signalement): Response
    {
        $image = null;
        if ($signalement->getImage()) {
            $imageContent = stream_get_contents($signalement->getImage());
            rewind($signalement->getImage());
            $image = base64_encode($imageContent);
        }
    
        return $this->render('signalement/show.html.twig', [
            'signalement' => $signalement,
            'image' => $image,
        ]);
    }

    #[Route('/{id_signalement}/edit', name: 'app_signalement_edit', methods: ['GET', 'POST'])]
    public function edit(
        Request $request, 
        Signalement $signalement, 
        EntityManagerInterface $entityManager
    ): Response {
        $form = $this->createForm(SignalementType::class, $signalement);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            /** @var UploadedFile $imageFile */
            $imageFile = $form->get('image')->getData();
            
            if ($imageFile) {
                try {
                    $imageContent = file_get_contents($imageFile->getPathname());
                    $signalement->setImage($imageContent);
                } catch (\Exception $e) {
                    $this->addFlash('error', 'Erreur lors du traitement de l\'image');
                    return $this->redirectToRoute('app_signalement_edit', ['id_signalement' => $signalement->getIdSignalement()]);
                }
            }
    
            $entityManager->flush();
    
            $this->addFlash('success', 'Signalement modifié avec succès!');
            return $this->redirectToRoute('app_signalement_index', [], Response::HTTP_SEE_OTHER);
        }
    
        // Préparation de l'image courante pour l'affichage
        $currentImage = null;
        if ($signalement->getImage()) {
            $imageContent = stream_get_contents($signalement->getImage());
            rewind($signalement->getImage());
            $currentImage = base64_encode($imageContent);
        }
    
        return $this->render('signalement/edit.html.twig', [
            'signalement' => $signalement,
            'form' => $form,
            'current_image' => $currentImage,
        ]);
    }


    #[Route('/{id_signalement}', name: 'app_signalement_delete', methods: ['POST'])]
    public function delete(Request $request, Signalement $signalement, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$signalement->getIdSignalement(), $request->request->get('_token'))) {
            $entityManager->remove($signalement);
            $entityManager->flush();
            $this->addFlash('success', 'Signalement supprimé avec succès!');
        } else {
            $this->addFlash('error', 'Token CSRF invalide');
        }

        return $this->redirectToRoute('app_signalement_index', [], Response::HTTP_SEE_OTHER);
    }
    
}