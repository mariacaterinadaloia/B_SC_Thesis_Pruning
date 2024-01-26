(define (domain prova)
    (:requirements :strips :negative-preconditions :typing)
    (:predicates
        (on ?s ?t)
        (to ?s ?t)
        (as ?s ?t)
        (super ?s ?t)
        (is ?s ?t)
        (has ?s ?t)
    )
    (:constants
        daloia
    )
    (:action aggiungiOggetto2
        :parameters (?s ?t)
        :precondition
            (and
                (not(to ?s ?t))
                (as ?s ?t)
            )
        :effect
            (and
                (to ?s ?t)
            )
    )

    (:action aggiungiOggetto3
                :parameters (?s ?t)
                :precondition
                (and
                    (not(to ?s ?t))
                    (not(as ?s ?t))
                )
                :effect
                (and
                    (to ?s ?t)
                )
    )

    (:action rimuoviOggetto
        :parameters (?s ?t)
        :precondition
        (and
            (on ?s ?t)
        )
        :effect
        (and
            (not (on ?s ?t))
        )
    )

    (:action aggiungiOggetto
            :parameters (?s ?t)
            :precondition
            (and
                (not(to ?s ?t))
            )
            :effect
            (and
                (to ?s ?t)
            )
    )

    (:action superAction
        :parameters (?s ?t)
        :precondition
            (and
                (super ?s ?t)
            )
        :effect
            (and
                (not(super ?s ?t))
            )
    )

    (:action delete1
    :parameters (?s ?t)
    :precondition
        (and
                (is ?s ?t)
                (has ?s ?t)
        )
    :effect (and
        (not(has ?s ?t))
        )
    )

    (:action delete2
        :precondition
            (and
                    (has ?s ?t)
            )
        :effect (and
            (not(has ?s ?t))
            )
        )
)