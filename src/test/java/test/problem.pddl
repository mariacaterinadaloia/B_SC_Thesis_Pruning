(define (problem provaP)
    (:domain prova)

    (:objects
        coso1
        coso2
    )

    (:init
        (super coso1 coso2)
        (not(on coso1 coso2))
        (to coso1 coso2)
        (as coso1 coso2)
    )
    (:goal
        (and
            (not(super coso1 coso2)
        )
    )
)
