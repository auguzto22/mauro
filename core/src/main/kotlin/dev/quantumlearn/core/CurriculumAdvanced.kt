package dev.quantumlearn.core

internal fun Curriculum.tunneling() = lesson(
    "tunneling", "Tunelamento quântico", "Explique como uma barreira finita pode transmitir uma onda de matéria.",
    "No modelo clássico, uma partícula de energia menor que a altura da barreira não a atravessa. Em mecânica quântica, a amplitude pode penetrar e emergir do outro lado.",
    "Em uma barreira retangular, aumentar a largura mantendo altura e energia fixas reduz a transmissão. Ela continua positiva para uma barreira finita e energia incidente positiva.",
    """\kappa=\frac{\sqrt{2m(V_{0}-E)}}{\hbar}""",
    "κ é a taxa de decaimento no interior da barreira, m é a massa, V₀ é a altura do potencial, E é a energia com 0 < E < V₀ e ℏ é a constante reduzida.",
    "Dentro da barreira, a solução geral combina exponenciais crescentes e decrescentes. Ajuste-a às regiões externas. Para uma barreira espessa, a transmissão é proporcional a exp(−2κL) com os demais parâmetros fixos.",
    Diagram.Barrier, "A linha da energia incidente fica abaixo do topo da barreira de largura finita.",
    "Tunelamento depende da solução de onda e das fronteiras. Uma largura maior geralmente reduz fortemente a passagem.",
    "Microscópios de tunelamento usam a forte dependência da corrente com a distância.",
    listOf(mit), listOf("schrodinger-stationary", "energy"),
    choose("Para 0 < E < V₀ fixo, o que acontece ao aumentar a largura de uma barreira retangular?", 1, listOf(
        wrong("A transmissão aumenta", "A amplitude se atenua por uma região mais extensa, o que reduz a transmissão.", "Atenuação na barreira"),
        correct("A transmissão diminui", "A largura maior reduz a transmissão para energia abaixo da barreira."),
        wrong("A energia da partícula diminui ao atravessar", "Em uma barreira estática com mesmo potencial externo, a energia se conserva.", "Transmissão e conservação de energia")
    ), "Fixe as energias e a massa, o que mantém κ constante.", "Aumentar a extensão da atenuação reduz a transmissão.",
        "Em uma barreira espessa, acrescentar um comprimento 1/κ reduz a transmissão aproximadamente por fator exp(−2)."),
    choose("Em lados externos com o mesmo potencial, compare a energia da onda transmitida à incidente para uma barreira estática.", 0, listOf(
        correct("É a mesma", "A altura afeta a transmissão; a energia total permanece E."),
        wrong("É sempre V₀", "O topo da barreira é a energia potencial de uma região e não a energia da onda transmitida.", "Energia potencial e energia total"),
        wrong("Cai a zero", "A onda transmitida se propaga do outro lado com a energia incidente.", "Amplitude e energia")
    ), "Identifique um potencial independente do tempo.", "Use conservação de energia para a onda transmitida.",
        "Mudar a altura da barreira muda a probabilidade de transmissão, mantendo a energia no regime estacionário.",
        difficulty = Difficulty.Intermediate))

internal fun Curriculum.entanglement() = lesson(
    "entanglement", "Emaranhamento", "Reconheça quando um estado puro conjunto deixa de fatorar.",
    "Dois sistemas formam um estado emaranhado quando seu estado puro conjunto não pode ser escrito como produto de um estado de cada sistema.",
    "Em um par preparado com amplitudes iguais para 00 e 11, medidas na base computacional produzem resultados iguais. Para diferenciar emaranhamento de correlação clássica, também examinamos outras bases.",
    """|s\rangle=\frac{|00\rangle+|11\rangle}{\sqrt{2}}""",
    "|s⟩ é o estado de dois qubits. |00⟩ e |11⟩ são resultados da base conjunta, e 1/√2 normaliza a soma.",
    "Em um estado produto, as amplitudes de 00, 01, 10 e 11 fatoram como produtos de dois pares. Manter 00 e 11 não nulos exigiria também 01 e 10 não nulos para um produto. Esse estado, portanto, é emaranhado.",
    Diagram.Matrix, "A grade 2 × 2 representa os quatro pares possíveis de resultados.",
    "Um estado conjunto pode ser bem definido mesmo quando as medidas individuais são aleatórias.",
    "Pares emaranhados permitem testar desigualdades de Bell e servem como recurso para protocolos quânticos.",
    listOf(mitSpin, feynman), listOf("spin", "quantum-states", "dirac"),
    choose("No estado (|00⟩ + |11⟩)/√2, qual é a distribuição na base computacional?", 1, listOf(
        wrong("25% para cada um dos quatro pares", "Os resultados 01 e 10 têm amplitude zero neste estado.", "Amplitudes ausentes"),
        correct("50% para 00 e 50% para 11", "Cada termo presente tem amplitude 1/√2."),
        wrong("Apenas 00", "O termo 11 também tem amplitude não nula e contribui com 1/2.", "Superposição e resultado certo")
    ), "Leia as amplitudes dos quatro pares.", "Calcule os módulos quadrados dos dois termos presentes.",
        "Trocando 00 e 11 por 01 e 10, os sinais medidos são sempre opostos na mesma base."),
    choose("O emaranhamento permite enviar uma mensagem alterando só a base de medida local?", 2, listOf(
        wrong("Sim, a outra pessoa escolhe qual resultado recebe", "O observador só controla sua escolha local de medida, não escolhe o resultado aleatório.", "Escolha de medida e escolha de resultado"),
        wrong("Sim, desde que o par seja puro", "A distribuição marginal remota permanece a mesma para uma escolha local de medida, independentemente da pureza.", "Marginal e correlação"),
        correct("Não, a distribuição local remota permanece igual", "Comparar correlações precisa de informação obtida de ambos os lados.")
    ), "Considere apenas os dados do observador remoto, sem receber o resultado local.",
        "A distribuição marginal desses dados não muda com a escolha da medida local.",
        "Para testar correlações do par, é necessário reunir os dois registros.", difficulty = Difficulty.Intermediate))

internal fun Curriculum.decoherence() = lesson(
    "decoherence", "Decoerência", "Explique a perda de interferência ao ignorar o ambiente.",
    "Um sistema pode se correlacionar com o ambiente. Quando não observamos o ambiente, a descrição apenas do sistema pode perder coerência entre alternativas.",
    "Se um marcador registra por qual fenda uma partícula passou em estados perfeitamente distinguíveis, as franjas somem nos dados que ignoram o marcador.",
    """\rho_{01}^{novo}=\rho_{01}^{inicial}\langle e_{1}|e_{0}\rangle""",
    "ρ₀₁ é o elemento fora da diagonal da matriz de densidade do sistema. |e₀⟩ e |e₁⟩ são os estados do ambiente associados às alternativas.",
    "Em um modelo de desfasagem, o produto interno entre estados do ambiente multiplica a coerência. Para estados ortogonais, o fator é zero. As populações diagonais podem permanecer iguais.",
    Diagram.Matrix, "Uma matriz 2 × 2 tem duas entradas na diagonal e duas fora dela. A desfasagem ideal reduz as últimas.",
    "Decoerência afeta a interferência observável ao ignorar graus de liberdade do ambiente.",
    "Controlar o acoplamento ambiental é necessário para preservar a fase de qubits.",
    listOf(mitSpin, feynman), listOf("entanglement", "double-slit", "matrices"),
    choose("Os estados do ambiente e₀ e e₁ são ortogonais. O que acontece com ρ₀₁ neste modelo?", 0, listOf(
        correct("Torna-se zero", "O produto interno entre estados ortogonais é zero."),
        wrong("Permanece igual", "Isso ocorreria para ambientes idênticos com produto interno um.", "Distinguibilidade do ambiente"),
        wrong("Muda apenas de sinal", "Uma fase de π exigiria produto interno −1. Ortogonalidade dá zero.", "Desfasagem e fase determinística")
    ), "Calcule ⟨e₁|e₀⟩ = 0.", "Multiplique o elemento inicial por zero.",
        "Se o produto interno for 1/2, a coerência fica reduzida à metade."),
    number("No modelo de desfasagem, ρ₀₁ inicialmente vale 0,4 e a sobreposição real do ambiente vale 0,5. Qual é o novo valor?",
        "0.2", "Aplique o fator de sobreposição à coerência inicial.", "0,4 × 0,5 = 0,2.",
        "Com sobreposição 1, o valor permaneceria 0,4.", difficulty = Difficulty.Advanced))

internal fun Curriculum.oscillator() = lesson(
    "oscillator", "Oscilador harmônico quântico", "Reconheça a energia do estado fundamental e os espaçamentos.",
    "Quando o potencial cresce como o quadrado da posição, o modelo é um oscilador harmônico. No caso quântico, estados estacionários têm energias igualmente espaçadas.",
    "Perto do equilíbrio, vibrações de uma ligação molecular podem se aproximar de um potencial quadrático. Essa aproximação melhora para pequenas oscilações.",
    """E_{n}=\hbar\omega(n+\frac{1}{2})""",
    "Eₙ é a energia do nível n, n começa em zero, ℏ é a constante de Planck reduzida e ω é a frequência angular.",
    "O potencial tem mínimo zero nessa escolha de energia. O estado fundamental tem energia ℏω/2. A cada unidade a mais de n corresponde um acréscimo ℏω.",
    Diagram.Levels, "As linhas de energia igualmente espaçadas mostram a diferença constante entre níveis vizinhos.",
    "O estado fundamental é elevado em meia unidade ℏω e a distância entre níveis é uma unidade.",
    "O oscilador é um modelo útil para vibrações e para modos de campos quânticos livres.",
    listOf(mit, qft), listOf("schrodinger-stationary", "energy"),
    choose("Qual energia tem o estado fundamental do oscilador harmônico, com mínimo do potencial em zero?", 2, listOf(
        wrong("Zero", "n = 0, mas o termo aditivo 1/2 permanece.", "Energia de ponto zero"),
        wrong("ℏω", "ℏω é o espaçamento entre níveis, não o valor do primeiro nível.", "Espaçamento e energia fundamental"),
        correct("ℏω/2", "Substitua n = 0 na expressão.")
    ), "Identifique o estado fundamental como n = 0.", "Mantenha o termo 1/2 em Eₙ.",
        "O primeiro excitado tem n = 1 e energia 3ℏω/2."),
    number("Para n = 3, qual é E₃ medida em unidades de ℏω?", "3.5",
        "Substitua n = 3 em n + 1/2.", "Some 3 + 0,5 = 3,5.", "Para n = 2 o resultado é 2,5."))

internal fun Curriculum.hydrogen() = lesson(
    "hydrogen", "Átomo de hidrogênio", "Estime energias ligadas e transições no modelo principal.",
    "No modelo não relativístico do hidrogênio com potencial coulombiano, estados ligados têm energia negativa quando escolhemos zero para a ionização.",
    "Elevar o elétron de n = 1 para n = 2 requer cerca de 10,2 eV. A energia sobe de −13,6 eV para −3,4 eV.",
    """E_{n}\approx-\frac{13{,}6\,\mathrm{eV}}{n^{2}}""",
    "Eₙ é a energia do nível ligado. n é o número quântico principal, um inteiro positivo. eV é a unidade elétron-volt.",
    "À medida que n cresce, as energias se aproximam de zero por valores negativos. A expressão aproximada omite estrutura fina, efeitos de spin e deslocamentos radiativos.",
    Diagram.Levels, "Linhas horizontais representam energias discretas. No hidrogênio, convergem para o limiar de ionização.",
    "Energias ligadas são negativas na referência adotada. Uma transição troca a diferença de energia.",
    "Linhas espectrais do hidrogênio são usadas em espectroscopia e astronomia.",
    listOf(mit, nist), listOf("schrodinger-stationary", "photons", "electricity"),
    choose("O que ocorre com Eₙ quando n aumenta no modelo principal do hidrogênio?", 1, listOf(
        wrong("Fica cada vez mais negativa", "Dividir o valor negativo por um denominador maior reduz seu módulo.", "Sinal e magnitude"),
        correct("Aumenta em direção a zero", "Eₙ é negativo e seu valor absoluto diminui."),
        wrong("Fica positiva já em n = 2", "O denominador é positivo; o numerador negativo mantém todos os níveis ligados abaixo de zero.", "Nível ligado e ionização")
    ), "Compare E₁ = −13,6 eV e E₂ = −3,4 eV.", "Observe que o valor aumenta, mas permanece negativo.",
        "E₃ vale aproximadamente −1,51 eV."),
    number("Com E₁ = −13,6 eV e E₂ = −3,4 eV, calcule a energia absorvida para subir de 1 a 2. Inclua a unidade.", "10.2",
        "Calcule energia final menos inicial.", "−3,4 − (−13,6) = 10,2 eV.",
        "A transição inversa pode emitir um fóton de 10,2 eV.", "eV"))

internal fun Curriculum.perturbation() = lesson(
    "perturbation", "Teoria da perturbação", "Calcule a correção de primeira ordem de uma energia não degenerada.",
    "Se conhecemos um Hamiltoniano simples e acrescentamos um efeito pequeno, podemos aproximar o novo resultado com correções em ordem crescente.",
    "Para um estado de energia isolada, somar um pequeno potencial constante δ ao Hamiltoniano acrescenta δ à energia.",
    """\Delta E^{(1)}=\langle n|V|n\rangle""",
    "ΔE⁽¹⁾ é a correção de primeira ordem, |n⟩ é o autoestado normalizado não perturbado e V é o operador da perturbação incluindo sua intensidade.",
    "A expressão é o valor esperado de V no estado original. Quando energias estão próximas ou degeneradas, a aproximação não degenerada pode falhar e requer tratamento diferente.",
    Diagram.Levels, "Linhas horizontais representam os níveis de energia antes de acrescentar a perturbação.",
    "Para um nível isolado, use o elemento diagonal da perturbação no autoestado original.",
    "A teoria estima deslocamentos de níveis causados por campos e por interações fracas.",
    listOf(mitSpin), listOf("schrodinger-stationary", "dirac"),
    choose("Qual elemento de V dá a correção de primeira ordem de Eₙ para um nível não degenerado?", 0, listOf(
        correct("⟨n|V|n⟩", "A correção de primeira ordem usa a diagonal no estado original."),
        wrong("⟨m|V|n⟩ com m diferente de n", "Um elemento fora da diagonal participa da mistura dos estados, não diretamente dessa correção de energia.", "Diagonal e mistura"),
        wrong("⟨n|n⟩", "Esse produto vale 1 por normalização. Falta incluir o operador da perturbação.", "Norma e valor esperado")
    ), "Identifique o autoestado do nível corrigido.", "Insira V entre o bra e o ket desse estado.",
        "Se ⟨n|V|n⟩ = 0, a correção de primeira ordem é zero; ordens superiores ainda podem contribuir."),
    number("Um nível de 5 eV recebe correção de primeira ordem de −0,2 eV. Qual é a energia aproximada? Inclua a unidade.", "4.8",
        "Some energia original e correção.", "5 + (−0,2) = 4,8 eV.",
        "Com energia 3 eV e correção +0,1 eV, obtemos cerca de 3,1 eV.", "eV", difficulty = Difficulty.Advanced))

internal fun Curriculum.quantumFields() = lesson(
    "quantum-fields", "Introdução à teoria quântica de campos", "Interprete partículas como excitações quantizadas dos modos de um campo livre.",
    "A teoria quântica de campos usa campos quantizados para descrever sistemas com processos de criação e aniquilação de partículas. Um modo livre pode se comportar como um oscilador quântico.",
    "A luz em uma cavidade ideal oferece modos discretos. Um modo pode ter zero, um ou mais fótons, com energias que diferem em passos definidos.",
    """\Delta E=\hbar\omega""",
    "ΔE é a diferença de energia ao acrescentar uma excitação a um modo bosônico livre. ℏ é a constante de Planck reduzida e ω é a frequência angular do modo.",
    "Em um campo livre, cada modo bosônico pode receber ocupações inteiras não negativas. Em campos interagentes, a imagem de modos independentes é uma aproximação. Modos fermiônicos têm regras distintas.",
    Diagram.Modes, "Picos separados ajudam a distinguir frequências de dois modos de uma cavidade.",
    "Em um modo bosônico ideal, um quantum acrescenta ℏω relativamente à energia anterior.",
    "A eletrodinâmica quântica usa campos de matéria e luz para descrever interações testadas experimentalmente.",
    listOf(qft), listOf("oscillator", "fourier", "photons"),
    choose("O que representa um fóton na descrição de um modo eletromagnético livre?", 1, listOf(
        wrong("Uma frequência clássica sem estado quântico", "A frequência identifica o modo. O fóton identifica um quantum de sua excitação.", "Frequência e número de ocupação"),
        correct("Uma excitação quantizada do modo", "Acrescentar um fóton aumenta a energia do modo em ℏω."),
        wrong("Uma pequena parte arbitrária da energia ℏω", "O número de ocupação tem valores inteiros e a diferença entre níveis adjacentes é ℏω.", "Quantização da ocupação")
    ), "Associe ao modo sua frequência angular ω.", "Identifique a diferença de energia ℏω por quantum.",
        "Em um modo de frequência duas vezes maior, cada excitação vale o dobro."),
    number("Para um modo livre, qual é a diferença de energia entre ocupações 4 e 1, em unidades de ℏω?", "3",
        "Subtraia os números de ocupação: 4 − 1.", "Cada passo vale uma unidade ℏω, então a diferença é 3.",
        "Entre 2 e 0, a diferença vale 2ℏω.", difficulty = Difficulty.Advanced))
