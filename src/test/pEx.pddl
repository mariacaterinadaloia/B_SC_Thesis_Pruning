(define (problem pEx)
    (:domain domainEx)
    (:objects 
        sica monetti - studente
        genovese - professore
        POO - esame
    )

    (:init
        (not(ha_superato sica POO))
        (not(ha_superato monetti POO))
        (detiene genovese POO)
        (not(bocciato sica POO))
    )

    (:goal 
        (and
            (ha_superato sica POO)
            (ha_superato monetti POO)
        )
    )
)
