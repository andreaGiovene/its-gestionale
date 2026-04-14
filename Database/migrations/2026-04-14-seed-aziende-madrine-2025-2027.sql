-- Seed aziende madrine annualita 2025/2027.
-- Compatibile con lo schema attuale della tabella public.azienda.
-- La colonna citta viene popolata solo se presente la migration dedicata.
-- La colonna logica is_madrina serve solo nel seed per rendere esplicita
-- l'associazione azienda <-> corso 2025/2027.

INSERT INTO public.azienda (ragione_sociale, partita_iva, telefono, email, indirizzo, cap, citta)
SELECT v.ragione_sociale, v.partita_iva, v.telefono, v.email, v.indirizzo, v.cap, v.citta
FROM (
    VALUES
        ('GBS - Global Business Solution S.r.l.', '08508360016', '011 750 93 27', 'info@gbsweb.it', 'Corso Svizzera 185, Centro Dir. Piero della Francesca, Fab. 1, 3° Piano, Scala L', '10149', 'Torino'),
        ('Synesthesia S.r.l.', '10502360018', '+39 011 04 37 401', 'info@synesthesia.it', 'Sede operativa: Corso Dante 118', '10126', 'Torino'),
        ('Kedos S.r.l.', '02629980349', '011 19466570', 'info@kedos-srl.it', 'Corso Inghilterra 49', '10138', 'Torino'),
        ('7Layers S.r.l.', '06225550489', '0571 1738106', NULL, 'Via Durandi 10', '10144', 'Torino'),
        ('Aizoon Consulting S.r.l.', '09220780010', '011 2344611', 'aizoon@aizoon.it', 'Strada del Lionetto 6', '10146', 'Torino'),
        ('Retelit S.p.A.', '12897160151', '02 2020451', 'marketing@retelit.it', 'Via Pola 9', '20124', 'Milano'),
        ('Corley S.r.l.', '10669790015', '+39 011 19458132', 'info@corley.it', 'Corso Castelfidardo 22', '10128', 'Torino'),
        ('Present S.p.A.', '06696370961', '02 300121', 'info@it-present.com', 'Via Spalato 68', '10141', 'Torino'),
        ('Horsa S.p.A.', '02415190400', '+39 051 0402211', 'events@horsa.com', 'Lingotto Center, Via Nizza 262/72', '10126', 'Torino'),
        ('Kairos3D S.r.l.', '10190870013', '011 3157111', NULL, 'Via Agostino da Montefeltro 2', '10134', 'Torino'),
        ('Eurix S.r.l.', '05939290010', '011 18925208', 'info@eurixgroup.com', 'Corso Vittorio Emanuele II 61', '10128', 'Torino'),
        ('Tesisquare S.p.A.', '02448510046', '011 3273179', NULL, 'Corso Unione Sovietica 612/21', '10135', 'Torino'),
        ('Kakashi Venture Accelerator S.r.l.', '12781800011', NULL, NULL, 'Via Sant''Antonino 17B', '10128', 'Torino'),
        ('MSC Technology (Italia) S.r.l.', '12321430014', NULL, NULL, 'Via Nizza 262 (Lingotto)', '10126', 'Torino')
) AS v(ragione_sociale, partita_iva, telefono, email, indirizzo, cap, citta)
WHERE NOT EXISTS (
    SELECT 1
    FROM public.azienda a
    WHERE a.partita_iva = v.partita_iva
);

-- Se la migration id_azienda_madrina e gia presente, collega i corsi 2025/2027
-- alle aziende madrine corrispondenti.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'corso'
          AND column_name = 'id_azienda_madrina'
    ) THEN
        UPDATE public.corso corso
        SET id_azienda_madrina = azienda.id
        FROM (
            VALUES
                ('Digital Strategist', 'GBS - Global Business Solution S.r.l.', TRUE),
                ('Web e Mobile App Developer', 'Synesthesia S.r.l.', TRUE),
                ('Software Developer', 'Kedos S.r.l.', TRUE),
                ('Cyber Security Specialist', '7Layers S.r.l.', TRUE),
                ('SOC Analyst', 'Aizoon Consulting S.r.l.', TRUE),
                ('Cloud Specialist', 'Retelit S.p.A.', TRUE),
                ('AWS Cloud Architect', 'Corley S.r.l.', TRUE),
                ('Business Intelligence Software Developer', 'Present S.p.A.', TRUE),
                ('Business Application Developer', 'Horsa S.p.A.', TRUE),
                ('AR/VR and 3D Artist', 'Kairos3D S.r.l.', TRUE),
                ('Data Analyst e AI Specialist', 'Eurix S.r.l.', TRUE),
                ('Full Stack Developer', 'Tesisquare S.p.A.', TRUE),
                ('GenAI Specialist', 'Kakashi Venture Accelerator S.r.l.', TRUE),
                ('Web Solutions Architect', 'MSC Technology (Italia) S.r.l.', TRUE)
        ) AS mapping(nome_corso, ragione_sociale, is_madrina)
        JOIN public.azienda azienda
            ON LOWER(azienda.ragione_sociale) = LOWER(mapping.ragione_sociale)
        WHERE corso.nome_corso = mapping.nome_corso
          AND corso.anno_accademico = '2025/2027';
    END IF;
END $$;
