<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use App\Repository\ReponseRepository;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ReponseRepository::class)]
#[ORM\Table(name: 'reponse')]
class Reponse
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(name: 'id_reponse', type: 'integer')]
    private ?int $id_reponse = null;

    #[ORM\ManyToOne(targetEntity: Signalement::class, inversedBy: 'reponses')]
    #[ORM\JoinColumn(name: 'id_signalement', referencedColumnName: 'id_signalement', onDelete: 'CASCADE')]
    #[Assert\NotNull(message: "Le signalement associé ne peut pas être vide")]
    private ?Signalement $signalement = null;

    #[ORM\Column(type: 'text')]
    #[Assert\NotBlank(message: "La réponse ne peut pas être vide")]
    #[Assert\Length(
        min: 10,
        max: 5000,
        minMessage: "La réponse doit contenir au moins {{ limit }} caractères",
        maxMessage: "La réponse ne peut pas dépasser {{ limit }} caractères"
    )]
    private ?string $reponse = null;

    #[ORM\Column(type: 'datetime')]
    #[Assert\NotNull(message: "La date de réponse ne peut pas être vide")]
    #[Assert\LessThanOrEqual(
        value: "now",
        message: "La date de réponse ne peut pas être dans le futur"
    )]
    private ?\DateTimeInterface $date_reponse = null;

    #[ORM\Column(type: 'string', length: 50)]
    #[Assert\NotBlank(message: "Le statut ne peut pas être vide")]
    #[Assert\Choice(
        choices: ['en_attente', 'en_cours', 'resolu'],
        message: "Le statut doit être l'un des suivants: en_attente, en_cours, resolu"
    )]
    private ?string $statut = 'en_attente';

    public function __construct()
    {
        $this->date_reponse = new \DateTime();
    }

    // Getters et Setters

    public function getIdReponse(): ?int
    {
        return $this->id_reponse;
    }

    public function getId_reponse(): ?int
    {
        return $this->id_reponse;
    }

    public function setId_reponse(int $id_reponse): static
    {
        $this->id_reponse = $id_reponse;
        return $this;
    }

    public function getSignalement(): ?Signalement
    {
        return $this->signalement;
    }

    public function setSignalement(?Signalement $signalement): static
    {
        $this->signalement = $signalement;
        return $this;
    }

    public function getReponse(): ?string
    {
        return $this->reponse;
    }

    public function setReponse(string $reponse): static
    {
        $this->reponse = $reponse;
        return $this;
    }

    public function getDateReponse(): ?\DateTimeInterface
    {
        return $this->date_reponse;
    }

    public function getDate_reponse(): ?\DateTimeInterface
    {
        return $this->date_reponse;
    }

    public function setDateReponse(\DateTimeInterface $date_reponse): static
    {
        $this->date_reponse = $date_reponse;
        return $this;
    }

    public function setDate_reponse(\DateTimeInterface $date_reponse): static
    {
        $this->date_reponse = $date_reponse;
        return $this;
    }

    public function getStatut(): ?string
    {
        return $this->statut;
    }

    public function setStatut(string $statut): static
    {
        $this->statut = $statut;
        return $this;
    }

    public function __toString(): string
    {
        return $this->reponse ? substr($this->reponse, 0, 50).'...' : 'Nouvelle Réponse';
    }
}