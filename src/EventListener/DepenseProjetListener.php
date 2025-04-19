<?php

namespace App\EventListener;

use App\Entity\DepenseProjet;
use Doctrine\ORM\Event\PostPersistEventArgs;
use Doctrine\ORM\Event\PostUpdateEventArgs;
use Doctrine\ORM\Event\PreRemoveEventArgs;

class DepenseProjetListener
{
    public function postPersist(DepenseProjet $depenseProjet, PostPersistEventArgs $event): void
    {
        $this->updateProjetDepense($depenseProjet, $event);
    }

    public function postUpdate(DepenseProjet $depenseProjet, PostUpdateEventArgs $event): void
    {
        $this->updateProjetDepense($depenseProjet, $event);
    }

    public function preRemove(DepenseProjet $depenseProjet, PreRemoveEventArgs $event): void
    {
        $projet = $depenseProjet->getProjet();
        if ($projet) {
            $projet->setDepense($projet->getDepense() - $depenseProjet->getMontant());
            $event->getObjectManager()->persist($projet);
        }
    }

    private function updateProjetDepense(DepenseProjet $depenseProjet, PostPersistEventArgs|PostUpdateEventArgs $event): void
    {
        $projet = $depenseProjet->getProjet();
        if ($projet) {
            $total = 0;
            foreach ($projet->getDepenseProjets() as $dp) {
                $total += $dp->getMontant();
            }
            $projet->setDepense($total);
            $event->getObjectManager()->persist($projet);
        }
    }
}