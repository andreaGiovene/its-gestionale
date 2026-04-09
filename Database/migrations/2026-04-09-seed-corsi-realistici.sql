-- Seed corsi ITS realistici per biennio.
-- Idempotente: evita duplicati confrontando (nome_corso, anno_accademico).

INSERT INTO public.corso (nome_corso, anno_accademico, stato)
SELECT v.nome_corso, v.anno_accademico, v.stato
FROM (
    VALUES
        ('AWS Cloud Architect', '2025/2027', 'ATTIVO'),
        ('AR/VR and 3D Artist', '2025/2027', 'ATTIVO'),
        ('Business Application Developer', '2025/2027', 'ATTIVO'),
        ('Business Intelligence Software Developer', '2025/2027', 'ATTIVO'),
        ('Cloud Specialist', '2025/2027', 'ATTIVO'),
        ('Cyber Security Specialist', '2025/2027', 'ATTIVO'),
        ('Data Analyst e AI Specialist', '2025/2027', 'ATTIVO'),
        ('Digital Strategist', '2025/2027', 'ATTIVO'),
        ('Full Stack Developer', '2025/2027', 'ATTIVO'),
        ('GenAI Specialist', '2025/2027', 'ATTIVO'),
        ('SOC Analyst', '2025/2027', 'ATTIVO'),
        ('Software Developer', '2025/2027', 'ATTIVO'),
        ('Web e Mobile App Developer', '2025/2027', 'ATTIVO'),
        ('Web Solutions Architect', '2025/2027', 'ATTIVO'),

        ('AR/VR and Game Developer', '2024/2026', 'ATTIVO'),
        ('Business Application Developer', '2024/2026', 'ATTIVO'),
        ('Business Intelligence Software Developer', '2024/2026', 'ATTIVO'),
        ('Cloud Security Specialist', '2024/2026', 'ATTIVO'),
        ('Cloud Specialist', '2024/2026', 'ATTIVO'),
        ('Cyber Security Specialist', '2024/2026', 'ATTIVO'),
        ('Data Analyst e AI Specialist', '2024/2026', 'ATTIVO'),
        ('Digital Strategist', '2024/2026', 'ATTIVO'),
        ('Full Stack Developer', '2024/2026', 'ATTIVO'),
        ('INFOR System Consultant', '2024/2026', 'ATTIVO'),
        ('Insurance Software Developer', '2024/2026', 'ATTIVO'),
        ('Mobile App Developer', '2024/2026', 'ATTIVO'),
        ('Software Developer', '2024/2026', 'ATTIVO'),
        ('SOC Analyst', '2024/2026', 'ATTIVO'),
        ('Web Developer', '2024/2026', 'ATTIVO'),
        ('Web e Mobile App Developer', '2024/2026', 'ATTIVO'),

        ('AR/VR and Game Developer', '2023/2025', 'CONCLUSO'),
        ('Cloud Security Specialist', '2023/2025', 'CONCLUSO'),
        ('Cloud Specialist', '2023/2025', 'CONCLUSO'),
        ('Cyber Security Specialist', '2023/2025', 'CONCLUSO'),
        ('Digital Strategist', '2023/2025', 'CONCLUSO'),
        ('ERP System Developer', '2023/2025', 'CONCLUSO'),
        ('FinTech Software Developer', '2023/2025', 'CONCLUSO'),
        ('Full Stack Developer', '2023/2025', 'CONCLUSO'),
        ('Mobile App Developer', '2023/2025', 'CONCLUSO'),
        ('Software Developer', '2023/2025', 'CONCLUSO'),
        ('Web Developer', '2023/2025', 'CONCLUSO'),

        ('AR/VR and Game Developer', '2022/2024', 'CONCLUSO'),
        ('Backend System Integrator', '2022/2024', 'CONCLUSO'),
        ('Cloud Specialist', '2022/2024', 'CONCLUSO'),
        ('Digital Strategist', '2022/2024', 'CONCLUSO'),
        ('ERP System Developer', '2022/2024', 'CONCLUSO'),
        ('FinTech Software Developer', '2022/2024', 'CONCLUSO'),
        ('ICT Security Specialist', '2022/2024', 'CONCLUSO'),
        ('Mobile App Developer', '2022/2024', 'CONCLUSO'),
        ('Web Developer', '2022/2024', 'CONCLUSO'),

        ('Backend System Integrator', '2021/2023', 'CONCLUSO'),
        ('Cloud Specialist', '2021/2023', 'CONCLUSO'),
        ('Digital Strategist', '2021/2023', 'CONCLUSO'),
        ('FinTech Software Developer', '2021/2023', 'CONCLUSO'),
        ('ICT Security Specialist', '2021/2023', 'CONCLUSO'),
        ('Mobile App e Gaming Developer', '2021/2023', 'CONCLUSO'),
        ('Web Developer', '2021/2023', 'CONCLUSO'),

        ('Backend System Integrator', '2020/2022', 'CONCLUSO'),
        ('Cloud Specialist', '2020/2022', 'CONCLUSO'),
        ('Digital Strategist', '2020/2022', 'CONCLUSO'),
        ('ICT Security Specialist', '2020/2022', 'CONCLUSO'),
        ('Web e Mobile App Developer', '2020/2022', 'CONCLUSO'),

        ('Backend System Integrator', '2019/2021', 'CONCLUSO'),
        ('Cloud Services, Big Data e IoT', '2019/2021', 'CONCLUSO'),
        ('Digital Strategist', '2019/2021', 'CONCLUSO'),
        ('ICT Security Specialist', '2019/2021', 'CONCLUSO'),
        ('Web e Mobile App Developer', '2019/2021', 'CONCLUSO'),

        ('ICT Security Specialist', '2018/2020', 'CONCLUSO'),
        ('Integrated Backend Services', '2018/2020', 'CONCLUSO'),
        ('Interaction & Visual Design', '2018/2020', 'CONCLUSO'),
        ('Web & Mobile App Development', '2018/2020', 'CONCLUSO'),

        ('Integrated Backend Services', '2017/2019', 'CONCLUSO'),
        ('Interaction & Visual Design', '2017/2019', 'CONCLUSO'),
        ('Web & Mobile App Development', '2017/2019', 'CONCLUSO'),

        ('Integrated Backend Services', '2016/2018', 'CONCLUSO'),
        ('Interaction & Visual Design', '2016/2018', 'CONCLUSO'),
        ('Web & Mobile App Development', '2016/2018', 'CONCLUSO')
) AS v(nome_corso, anno_accademico, stato)
WHERE NOT EXISTS (
    SELECT 1
    FROM public.corso c
    WHERE c.nome_corso = v.nome_corso
      AND c.anno_accademico = v.anno_accademico
);
