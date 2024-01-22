(define (domain libretto)
    (:requirements :strips :typing :negative-preconditions)
    (:types
        studente
        impiegato 
        professore - impiegato
        esame 
    )

    
    (:constants 
        daloia - studente
    )

    (:predicates
        (salto_appello ?s - studente ?e - esame)
        (bocciato ?s - studente ?e - esame)
        (ha_superato ?s - studente ?e - esame)
        (detiene ?p - professore ?e - esame)
    )

    (:action esameConvalidato
        :parameters (?r - studente ?s - professore ?t - esame)
        :precondition 
        (and 
            (detiene ?s ?t)
            (not (ha_superato ?r ?t))
        )
        :effect
        (and 
            (ha_superato ?r ?t)
        )
    )

    (:action bocciato
            :parameters (?r - studente ?s - professore ?t - esame)
            :precondition
            (and
                (bocciato ?r ?t)
            )
            :effect
            (and
                (not(bocciato ?r ?t))
            )
        )

        (:action esempio1
            :parameters (?r - studente ?s - professore ?t - esame)
                    :precondition
                    (and
                        (not (ha_superato ?r ?t))
                    )
                    :effect
                    (and
                        (ha_superato ?r ?t)
                    )
        )

        (:action esempio2
                    :parameters (?r - studente ?s - professore ?t - esame)
                            :precondition
                            (and
                                (ha_superato ?r ?t)
                            )
                            :effect
                            (and
                                (not(ha_superato ?r ?t))
                            )
                )


        (:action esempio3
                            :parameters (?r - studente ?s - professore ?t - esame)
                                    :precondition
                                    (and
                                        (salto_appello ?s - studente ?e - esame)
                                    )
                                    :effect
                                    (and
                                        (not(salto_appello ?s - studente ?e - esame))
                                    )
                        )

        (:action esempi4
                    :parameters (?r - studente ?s - professore ?t - esame)
                            :precondition
                            (and
                                (not (ha_superato ?r ?t))
                            )
                            :effect
                            (and
                                (ha_superato ?r ?t)
                            )
                )
)