-- Rimuove i corsi fittizi presenti nel dump iniziale.
-- Match esatto su id + contenuto, per evitare cancellazioni accidentali.

BEGIN;

-- Sgancia eventuali riferimenti dagli allievi prima della delete.
WITH corsi_fittizi(id, nome_corso, anno_accademico, stato) AS (
    VALUES
        (1,  'Cloud Developer',           '2024-2026', 'In corso'),
        (2,  'Cybersecurity Specialist',  '2023-2025', 'In corso'),
        (3,  'Data Analyst',              '2024-2026', 'In corso'),
        (4,  'Software Architect',        '2023-2025', 'Concluso'),
        (5,  'Digital Strategy',          '2024-2026', 'In corso'),
        (6,  'Cloud Developer',           '2024-2026', 'In corso'),
        (7,  'Cybersecurity Specialist',  '2023-2025', 'In corso'),
        (8,  'Data Analyst',              '2024-2026', 'In corso'),
        (9,  'Software Architect',        '2023-2025', 'Concluso'),
        (10, 'Digital Strategy',          '2024-2026', 'In corso'),
        (11, 'Cloud Developer',           '2024-2026', 'In corso'),
        (12, 'Cybersecurity Specialist',  '2023-2025', 'In corso'),
        (13, 'Data Analyst',              '2024-2026', 'In corso'),
        (14, 'Software Architect',        '2023-2025', 'Concluso'),
        (15, 'Digital Strategy',          '2024-2026', 'In corso'),
        (16, 'Backend Developer',         '2024-2026', 'In corso'),
        (17, 'Frontend Specialist',       '2024-2026', 'In corso'),
        (18, 'AI & Machine Learning',     '2023-2025', 'In corso'),
        (19, 'System Administrator',      '2023-2025', 'Concluso'),
        (20, 'Project Manager ICT',       '2024-2026', 'In corso'),
        (21, 'Social Media Manager',      '2023-2025', 'In corso'),
        (22, 'Big Data Architect',        '2024-2026', 'In corso'),
        (23, 'Mobile Developer',          '2023-2025', 'Concluso'),
        (24, 'UI/UX Designer',            '2024-2026', 'In corso'),
        (25, 'Blockchain Expert',         '2024-2026', 'In corso'),
        (26, 'Business Intelligence Software Developer', '2024-2026', 'In corso')
)
UPDATE public.allievo a
SET corso_id = NULL
WHERE EXISTS (
    SELECT 1
    FROM public.corso c
    INNER JOIN corsi_fittizi f
        ON c.id = f.id
       AND c.nome_corso = f.nome_corso
       AND c.anno_accademico = f.anno_accademico
       AND c.stato = f.stato
    WHERE a.corso_id = c.id
);

WITH corsi_fittizi(id, nome_corso, anno_accademico, stato) AS (
    VALUES
        (1,  'Cloud Developer',           '2024-2026', 'In corso'),
        (2,  'Cybersecurity Specialist',  '2023-2025', 'In corso'),
        (3,  'Data Analyst',              '2024-2026', 'In corso'),
        (4,  'Software Architect',        '2023-2025', 'Concluso'),
        (5,  'Digital Strategy',          '2024-2026', 'In corso'),
        (6,  'Cloud Developer',           '2024-2026', 'In corso'),
        (7,  'Cybersecurity Specialist',  '2023-2025', 'In corso'),
        (8,  'Data Analyst',              '2024-2026', 'In corso'),
        (9,  'Software Architect',        '2023-2025', 'Concluso'),
        (10, 'Digital Strategy',          '2024-2026', 'In corso'),
        (11, 'Cloud Developer',           '2024-2026', 'In corso'),
        (12, 'Cybersecurity Specialist',  '2023-2025', 'In corso'),
        (13, 'Data Analyst',              '2024-2026', 'In corso'),
        (14, 'Software Architect',        '2023-2025', 'Concluso'),
        (15, 'Digital Strategy',          '2024-2026', 'In corso'),
        (16, 'Backend Developer',         '2024-2026', 'In corso'),
        (17, 'Frontend Specialist',       '2024-2026', 'In corso'),
        (18, 'AI & Machine Learning',     '2023-2025', 'In corso'),
        (19, 'System Administrator',      '2023-2025', 'Concluso'),
        (20, 'Project Manager ICT',       '2024-2026', 'In corso'),
        (21, 'Social Media Manager',      '2023-2025', 'In corso'),
        (22, 'Big Data Architect',        '2024-2026', 'In corso'),
        (23, 'Mobile Developer',          '2023-2025', 'Concluso'),
        (24, 'UI/UX Designer',            '2024-2026', 'In corso'),
        (25, 'Blockchain Expert',         '2024-2026', 'In corso'),
        (26, 'Business Intelligence Software Developer', '2024-2026', 'In corso')
)
DELETE FROM public.corso c
USING corsi_fittizi f
WHERE c.id = f.id
  AND c.nome_corso = f.nome_corso
  AND c.anno_accademico = f.anno_accademico
  AND c.stato = f.stato;

COMMIT;
