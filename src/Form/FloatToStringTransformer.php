<?php
namespace App\Form;


class FloatToStringTransformer implements DataTransformerInterface
{
    public function transform($value)
    {
        return $value === null ? null : (string)$value;
    }

    public function reverseTransform($value)
    {
        return $value === null ? null : (float)$value;
    }
}