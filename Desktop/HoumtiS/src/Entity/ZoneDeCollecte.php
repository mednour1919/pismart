<?php

namespace App\Entity;

use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Symfony\Component\Validator\Constraints as Assert;
use App\Repository\ZoneDeCollecteRepository;

#[ORM\Entity(repositoryClass: ZoneDeCollecteRepository::class)]
#[ORM\Table(name: 'zone_de_collecte')]
class ZoneDeCollecte
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
    #[Assert\NotBlank(message: "Le nom de la zone ne peut pas être vide.")]
    #[Assert\Length(
        min: 2,
        max: 100,
        minMessage: "Le nom doit faire au moins {{ limit }} caractères.",
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z0-9\s\-éèêëàâäôöûüçÉÈÊËÀÂÄÔÖÛÜÇ]+$/",
        message: "Le nom ne peut contenir que des lettres, chiffres, espaces et tirets."
    )]
    private ?string $nom = null;

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    #[ORM\Column(type: 'integer', nullable: true)]
    #[Assert\PositiveOrZero(message: "La population doit être un nombre positif ou zéro.")]
    #[Assert\LessThan(
        value: 10000000,
        message: "La population ne peut excéder {{ value }} habitants."
    )]
    private ?int $population = null;

    public function getPopulation(): ?int
    {
        return $this->population;
    }

    public function setPopulation(?int $population): self
    {
        $this->population = $population;
        return $this;
    }

    #[ORM\Column(type: 'time', nullable: true)]
    #[Assert\Type(
        type: 'DateTimeInterface',
        message: "La valeur {{ value }} n'est pas un temps valide."
    )]
    private ?\DateTimeInterface $temps_de_collecte = null;

    public function getTempsDeCollecte(): ?\DateTimeInterface
    {
        return $this->temps_de_collecte;
    }

    public function setTempsDeCollecte(?\DateTimeInterface $temps_de_collecte): static
    {
        $this->temps_de_collecte = $temps_de_collecte;
        return $this;
    }

   
}