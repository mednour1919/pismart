<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use App\Repository\CamionRepository;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: CamionRepository::class)]
#[ORM\Table(name: 'camion')]
class Camion
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function setId(int $id): self
    {
        $this->id = $id;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    #[Assert\NotBlank(message: "Le type du camion ne peut pas être vide")]
    #[Assert\Length(
        min: 2,
        max: 50,
        minMessage: "Le type doit contenir au moins {{ limit }} caractères",
        maxMessage: "Le type ne peut pas dépasser {{ limit }} caractères"
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z0-9\s\-]+$/",
        message: "Le type ne peut contenir que des lettres, chiffres, espaces et tirets"
    )]
    private ?string $type = null;

    public function getType(): ?string
    {
        return $this->type;
    }

    public function setType(string $type): self
    {
        $this->type = $type;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: true)]
#[Assert\Choice(
    choices: ['en panne', 'en maintenance', 'en service', 'hors service'],
    message: "Le statut doit être l'un des suivants: en panne, en maintenance, en service, hors service"
)]
#[Assert\Length(
    max: 20,
    maxMessage: "Le statut ne peut pas dépasser {{ limit }} caractères"
)]
private ?string $statut = null;

public function getStatut(): ?string
{
    return $this->statut;
}

public function setStatut(?string $statut): self
{
    $this->statut = $statut;
    return $this;
}
    #[ORM\Column(type: 'integer', nullable: true)]
    #[Assert\PositiveOrZero(message: "La capacité doit être un nombre positif ou zéro")]
    #[Assert\LessThanOrEqual(
        value: 1000,
        message: "La capacité ne peut pas dépasser {{ compared_value }}"
    )]
    private ?int $capacity = null;

    public function getCapacity(): ?int
    {
        return $this->capacity;
    }

    public function setCapacity(?int $capacity): self
    {
        $this->capacity = $capacity;
        return $this;
    }

    #[ORM\Column(type: 'string', length: 255, nullable: true)]
    private ?string $image = null;

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(?string $image): self
    {
        $this->image = $image;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: true)]
    #[Assert\Length(
        min: 2,
        max: 100,
        minMessage: "Le nom doit contenir au moins {{ limit }} caractères",
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères"
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z0-9\s\-']+$/",
        message: "Le nom ne peut contenir que des lettres, chiffres, espaces, tirets et apostrophes"
    )]
    private ?string $nom = null;

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(?string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }
}