<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use App\Repository\StationRepository;

#[ORM\Entity(repositoryClass: StationRepository::class)]
#[ORM\Table(name: 'station')]
class Station
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id_station = null;

    #[ORM\Column(type: 'string', nullable: false)]
    #[Assert\NotBlank(message: 'Station name cannot be blank')]
    #[Assert\Length(max: 255, maxMessage: 'Station name cannot be longer than 255 characters')]
    private ?string $nomStation = null;

    #[ORM\Column(type: 'integer', nullable: false)]
    #[Assert\NotBlank(message: 'Capacity cannot be blank')]
    #[Assert\Positive(message: 'Capacity must be a positive number')]
    private ?int $capacite = null;

    #[ORM\Column(type: 'string', nullable: false)]
    #[Assert\NotBlank(message: 'Zone cannot be blank')]
    #[Assert\Length(max: 255, maxMessage: 'Zone cannot be longer than 255 characters')]
    private ?string $zone = null;

    #[ORM\Column(type: 'string', nullable: false)]
    #[Assert\NotBlank(message: 'Status cannot be blank')]
    #[Assert\Choice(choices: ['active', 'inactive'], message: 'Status must be either "active" or "inactive"')]
    private ?string $status = null;

    public function getId_station(): ?int
    {
        return $this->id_station;
    }

    public function setId_station(int $id_station): self
    {
        $this->id_station = $id_station;
        return $this;
    }

    public function getNomStation(): ?string
    {
        return $this->nomStation;
    }

    public function setNomStation(string $nomStation): self
    {
        $this->nomStation = $nomStation;
        return $this;
    }

    public function getCapacite(): ?int
    {
        return $this->capacite;
    }

    public function setCapacite(int $capacite): self
    {
        $this->capacite = $capacite;
        return $this;
    }

    public function getZone(): ?string
    {
        return $this->zone;
    }

    public function setZone(string $zone): self
    {
        $this->zone = $zone;
        return $this;
    }

    public function getStatus(): ?string
    {
        return $this->status;
    }

    public function setStatus(string $status): self
    {
        $this->status = $status;
        return $this;
    }

    public function getIdStation(): ?int
    {
        return $this->id_station;
    }
}
