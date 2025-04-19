<?php

namespace App\Entity;

use App\Repository\ContributionRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ContributionRepository::class)]
#[ORM\Table(name: 'contribution')]
class Contribution
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\ManyToOne(targetEntity: Projet::class, inversedBy: 'contributions')]
    #[ORM\JoinColumn(name: 'projet_id', referencedColumnName: 'id')]
    #[Assert\NotNull(message: "Le projet est obligatoire")]
    private ?Projet $projet = null;

    #[ORM\Column(type: 'text', nullable: false)]
    #[Assert\NotBlank(message: "Le message ne peut pas être vide")]
    #[Assert\Length(
        min: 10,
        max: 1000,
        minMessage: "Le message doit contenir au moins {{ limit }} caractères",
        maxMessage: "Le message ne peut pas dépasser {{ limit }} caractères"
    )]
    private ?string $message = null;

    #[ORM\Column(
        type: 'string',
        columnDefinition: "ENUM('idée', 'signalement', 'feedback') NOT NULL"
    )]
    #[Assert\NotBlank(message: "Le type est obligatoire")]
    #[Assert\Choice(
        choices: ['idée', 'signalement', 'feedback'],
        message: "Choisissez un type valide"
    )]
    private ?string $type = null;

    #[ORM\Column(type: 'datetime', nullable: true, name: 'date_creation')]
    private ?\DateTimeInterface $dateCreation = null;

    #[ORM\Column(type: 'string', nullable: true)]
    #[Assert\Choice(
        choices: ['en_attente', 'validé', 'rejeté'],
        message: "Statut invalide"
    )]
    private ?string $statut = null;

    // Getters / Setters

    public function getId(): ?int
    {
        return $this->id;
    }

    public function setId(int $id): self
    {
        $this->id = $id;
        return $this;
    }

    public function getProjet(): ?Projet
    {
        return $this->projet;
    }

    public function setProjet(?Projet $projet): self
    {
        $this->projet = $projet;
        return $this;
    }

    public function getMessage(): ?string
    {
        return $this->message;
    }

    public function setMessage(string $message): self
    {
        $this->message = $message;
        return $this;
    }

    public function getType(): ?string
    {
        return $this->type;
    }

    public function setType(string $type): self
    {
        $this->type = $type;
        return $this;
    }

    public function getDateCreation(): ?\DateTimeInterface
    {
        return $this->dateCreation;
    }

    public function setDateCreation(?\DateTimeInterface $dateCreation): self
    {
        $this->dateCreation = $dateCreation;
        return $this;
    }

    public function getStatut(): ?string
    {
        return $this->statut;
    }

    public function setStatut(?string $statut): self
    {
        $this->statut = $statut;
        return $this;
    }
}
