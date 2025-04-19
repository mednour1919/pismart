<?php

namespace App\Form;

use App\Entity\ZoneDeCollecte;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TimeType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\PositiveOrZero;

class ZoneDeCollecteType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', null, [
                'label' => 'Nom de la zone',
                'constraints' => [new NotBlank()],
                'attr' => ['placeholder' => 'Entrez le nom de la zone']
            ])
            ->add('population', null, [
                'label' => 'Population totale',
                'constraints' => [new NotBlank(), new PositiveOrZero()],
                'attr' => [
                    'min' => 0,
                    'placeholder' => 'Nombre d\'habitants'
                ],
                'help' => 'La population totale de la zone de collecte'
            ])
            ->add('temps_de_collecte', TimeType::class, [
                'widget' => 'single_text',
                'label' => 'Durée de collecte',
                'html5' => true,
                'attr' => ['class' => 'timepicker']
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => ZoneDeCollecte::class,
        ]);
    }
}