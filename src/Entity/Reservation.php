<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\ReservationRepository;

#[ORM\Entity(repositoryClass: ReservationRepository::class)]
#[ORM\Table(name: 'reservation')]
class Reservation
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $idReservation = null;

    public function getIdReservation(): ?int
    {
        return $this->idReservation;
    }

    public function setIdReservation(int $idReservation): self
    {
        $this->idReservation = $idReservation;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $numPLACE = null;

    public function getNumPLACE(): ?int
    {
        return $this->numPLACE;
    }

    public function setNumPLACE(int $numPLACE): self
    {
        $this->numPLACE = $numPLACE;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $date_Reservation = null;

    public function getDate_Reservation(): ?string
    {
        return $this->date_Reservation;
    }

    public function setDate_Reservation(string $date_Reservation): self
    {
        $this->date_Reservation = $date_Reservation;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: false)]
    private ?int $temps = null;

    public function getTemps(): ?int
    {
        return $this->temps;
    }

    public function setTemps(int $temps): self
    {
        $this->temps = $temps;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $marque = null;

    public function getMarque(): ?string
    {
        return $this->marque;
    }

    public function setMarque(string $marque): self
    {
        $this->marque = $marque;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: true)]
    private ?int $idStation = null;

    public function getIdStation(): ?int
    {
        return $this->idStation;
    }

    public function setIdStation(?int $idStation): self
    {
        $this->idStation = $idStation;
        return $this;
    }

    public function getDateReservation(): ?string
    {
        return $this->date_Reservation;
    }

    public function setDateReservation(string $date_Reservation): static
    {
        $this->date_Reservation = $date_Reservation;

        return $this;
    }

}
