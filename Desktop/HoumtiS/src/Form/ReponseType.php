<?php

namespace App\Form;

use App\Entity\Reponse;
use App\Entity\Signalement;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\DateTimeType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class ReponseType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('reponse', TextareaType::class, [
                'label' => 'Contenu de la réponse',
                'attr' => ['rows' => 5],
                'required' => true
            ])
            ->add('date_reponse', DateTimeType::class, [
                'widget' => 'single_text',
                'label' => 'Date de réponse',
                'required' => false
            ])
            ->add('statut', ChoiceType::class, [
                'choices' => [
                    'En cours' => 'en_cours',
                    'Traité' => 'traite',
                    'Clos' => 'clos'
                ],
                'label' => 'Statut',
                'required' => true
            ]);

        if ($options['include_signalement_field']) {
            $builder->add('signalement', EntityType::class, [
                'class' => Signalement::class,
                'label' => 'Signalement associé',
                'placeholder' => 'Sélectionnez un signalement',
                'required' => true
            ]);
        }
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Reponse::class,
            'include_signalement_field' => true
        ]);
    }
}