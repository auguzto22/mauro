package dev.quantumlearn.core

internal fun Curriculum.superposition() = lesson(
    "superposition", "Superposição", "Distinga uma soma coerente de uma mistura probabilística.",
    "Uma superposição combina estados por amplitudes. Suas fases relativas podem afetar a probabilidade quando medimos numa base que reúne contribuições.",
    "As contribuições +1/√2 e −1/√2 definem um estado diferente das contribuições +1/√2 e +1/√2. Em uma base apropriada, distinguimos os dois com certeza.",
    """|s\rangle=a|u\rangle+b|v\rangle""",
    "|s⟩ é o estado. |u⟩ e |v⟩ são estados ortonormais. a e b são amplitudes com |a|² + |b|² = 1.",
    "A fase relativa muda termos cruzados quando projetamos em outra base. Uma mistura clássica dos estados u e v usa probabilidades e não preserva esses termos.",
    Diagram.Interference, "As franjas ilustram o efeito de somar amplitudes coerentes antes de calcular probabilidades.",
    "A fase relativa carrega informação acessível em medições adequadas.",
    "Portas quânticas mudam amplitudes de forma coerente para controlar interferência.",
    listOf(mit, feynman), listOf("born", "double-slit"),
    choose("Um estado (|u⟩ + |v⟩)/√2 e outro (|u⟩ − |v⟩)/√2 têm as mesmas probabilidades em u e v. Eles são o mesmo estado físico?", 2, listOf(
        wrong("Sim, porque as probabilidades em uma base são iguais", "Uma única base perde informação de fase. Outras bases distinguem os estados.", "Probabilidade em uma base e estado completo"),
        wrong("Sim, porque a diferença é uma fase global", "Uma fase global multiplicaria ambos os termos. Aqui apenas o sinal relativo muda.", "Fase global e relativa"),
        correct("São distintos e uma medida em outra base os distingue", "Os dois estados são ortogonais, então uma base pode separá-los.")
    ), "Compare os sinais dos dois coeficientes.", "Observe que apenas um sinal muda; é uma fase relativa.",
        "Multiplicar ambos os coeficientes por −1 preserva o estado físico puro."),
    number("Para duas amplitudes reais iguais em um estado normalizado, qual é a probabilidade de um desses resultados na própria base?",
        "1/2", "Como as amplitudes têm módulo igual, as duas probabilidades são iguais.",
        "A soma é um. Cada uma vale 1/2.", "Para três amplitudes de mesmo módulo, cada probabilidade é 1/3."))

internal fun Curriculum.quantumStates() = lesson(
    "quantum-states", "Estados quânticos", "Determine se um vetor de amplitudes já está normalizado.",
    "Um estado puro descreve a preparação de um sistema. Em uma base ortonormal, escrevemos suas amplitudes como um vetor de norma um.",
    "O par (3, 4) tem norma 5. Como amplitudes reais, o par (3/5, 4/5) já fornece probabilidades que somam um.",
    """|a|^{2}+|b|^{2}=1""",
    "a e b são amplitudes de dois estados ortonormais. As barras indicam módulo e 1 é a soma das probabilidades.",
    "Para normalizar um vetor não nulo, divida cada amplitude pela norma. Vetores que diferem apenas por fase global representam o mesmo estado puro.",
    Diagram.Vector, "Um vetor no plano fornece uma analogia geométrica para as componentes reais e a norma.",
    "Calcule a norma e divida todas as amplitudes pelo mesmo valor, preservando suas relações.",
    "A normalização é uma checagem básica em simulação e preparação de estados quânticos.",
    listOf(mit), listOf("complex", "linear-algebra", "superposition"),
    choose("Que par normaliza o vetor (3, 4) preservando sua direção?", 0, listOf(
        correct("(3/5, 4/5)", "A soma dos quadrados é (9 + 16)/25 = 1."),
        wrong("(3/7, 4/7)", "Dividir pela soma dos componentes não dá norma um. Use a raiz da soma de quadrados.", "Soma e norma"),
        wrong("(9/25, 16/25)", "São as probabilidades do estado normalizado. As amplitudes, antes do quadrado, são 3/5 e 4/5.", "Amplitudes e probabilidades")
    ), "Calcule √(9 + 16) = 5.", "Divida as duas amplitudes por 5.",
        "O vetor (0, 2) vira (0, 1) ao dividir pela norma."),
    number("Qual é a norma de (0, −3)?", "3", "Calcule a soma dos quadrados, 0² + (−3)² = 9.",
        "A raiz principal de 9 vale 3.", "A norma de (−4, 0) vale 4."))

internal fun Curriculum.operators() = lesson(
    "operators", "Operadores", "Aplique um operador linear a um estado de dois componentes.",
    "Um operador transforma um vetor ou função em outro. Operadores lineares preservam combinações lineares, e numa base finita podem ser escritos como matrizes.",
    "O operador X de um qubit troca os estados |0⟩ e |1⟩. Aplicá-lo a (a, b) troca as duas amplitudes.",
    """X|0\rangle=|1\rangle\quad X|1\rangle=|0\rangle""",
    "X é o operador de troca. |0⟩ e |1⟩ são os vetores da base computacional.",
    "Pela linearidade, X(a|0⟩ + b|1⟩) = a|1⟩ + b|0⟩. Na ordem padrão dos componentes, o resultado é (b, a).",
    Diagram.Matrix, "Um arranjo de linhas e colunas pode codificar como um operador combina componentes.",
    "Aja sobre cada vetor da base, mantendo o coeficiente, e some as saídas.",
    "Operadores unitários descrevem portas ideais, e operadores autoadjuntos representam observáveis no formalismo usual.",
    listOf(mit, linear), listOf("matrices", "quantum-states"),
    choose("Aplique X a (3/5, 4/5). Qual é o vetor resultante?", 1, listOf(
        wrong("(3/5, 4/5)", "Isso seria aplicar a identidade. X troca as contribuições de |0⟩ e |1⟩.", "Identidade e transformação"),
        correct("(4/5, 3/5)", "O segundo componente passa para o primeiro e vice-versa."),
        wrong("(9/25, 16/25)", "Esses valores são módulos quadrados das amplitudes iniciais. Aplicar X é uma transformação linear.", "Operador e cálculo de probabilidades")
    ), "Mova a amplitude de |0⟩ para |1⟩.", "Mova a de |1⟩ para |0⟩ e escreva na ordem da base.",
        "Em (1, 0), a troca produz (0, 1)."),
    number("O operador X é aplicado duas vezes a (1, 0). Qual é a primeira componente ao final?", "1",
        "A primeira aplicação dá (0, 1).", "A segunda troca recupera (1, 0).",
        "O mesmo par de trocas retorna qualquer vetor (a, b) ao original."))

internal fun Curriculum.eigenvalues() = lesson(
    "eigenvalues", "Autovalores", "Identifique o fator associado a um autovetor.",
    "Um autovetor é um vetor não nulo que, após a ação de um operador, se torna um múltiplo de si mesmo. Esse fator é o autovalor.",
    "Se uma transformação estica apenas a direção horizontal por fator 2, um vetor horizontal é autovetor com autovalor 2.",
    """Av=\lambda v""",
    "A é o operador, v é o autovetor não nulo e λ é o autovalor.",
    "Autovalor negativo inverte o sentido. Se A for um observável e o sistema estiver num autoestado normalizado, a medida ideal desse observável retorna seu autovalor com certeza.",
    Diagram.Vector, "Mudar o comprimento do vetor mantendo a linha de sua direção ilustra uma transformação de autovetor.",
    "Confira se a saída está na mesma reta que o vetor de entrada e calcule o fator com sinal.",
    "Autovalores do Hamiltoniano são energias permitidas de seus estados ligados estacionários.",
    listOf(linear, mit), listOf("operators"),
    choose("Para um vetor não nulo v, Av = −2v. Qual é o autovalor?", 2, listOf(
        wrong("2", "A magnitude é 2, mas o sinal registra a inversão do vetor.", "Magnitude e sinal"),
        wrong("v", "v é o autovetor. O autovalor é o fator numérico que multiplica v.", "Autovetor e autovalor"),
        correct("−2", "A relação está na forma Av = λv com λ = −2.")
    ), "Identifique o vetor comum v.", "Leia o fator −2.",
        "Quando Av = 0 e v ≠ 0, zero é o autovalor."),
    number("Qual é o autovalor de v = (1, 0) se A(1, 0) = (3, 0)?", "3",
        "Escreva (3, 0) como 3(1, 0).", "Compare com Av = λv. O fator é 3.",
        "Se a saída fosse (−4, 0), o autovalor seria −4."))

internal fun Curriculum.diracNotation() = lesson(
    "dirac", "Notação de Dirac", "Use bras e kets para calcular uma projeção.",
    "Kets representam vetores de estado. O bra correspondente troca o vetor coluna por uma linha e toma o conjugado complexo de seus componentes.",
    "Na base |0⟩, |1⟩, o produto interno ⟨0|1⟩ é zero porque os vetores são ortogonais.",
    """\langle 0|\psi\rangle=a""",
    "⟨0| é o bra correspondente a |0⟩. |ψ⟩ = a|0⟩ + b|1⟩, com a e b complexos. O produto interno seleciona a.",
    "Se |ψ⟩ tem coeficiente i, o bra ⟨ψ| tem coeficiente correspondente −i. Essa conjugação garante que ⟨ψ|ψ⟩ é a soma dos módulos ao quadrado.",
    Diagram.Vector, "A projeção sobre um eixo da base ajuda a visualizar o papel do produto interno.",
    "Um bra e um ket formam um produto interno escalar. A ordem inversa produz um operador.",
    "A notação compacta conecta estados, amplitudes de medida e operadores.",
    listOf(mit), listOf("quantum-states", "eigenvalues"),
    choose("O que resulta de ⟨1|ψ⟩ se |ψ⟩ = a|0⟩ + b|1⟩ em uma base ortonormal?", 1, listOf(
        wrong("a", "O bra ⟨1| seleciona a direção |1⟩. O coeficiente a acompanha |0⟩.", "Projeção na base"),
        correct("b", "⟨1|0⟩ é zero e ⟨1|1⟩ é um, deixando b."),
        wrong("|b|²", "Esse é o valor da probabilidade após o módulo quadrado. O produto interno dá a amplitude b.", "Amplitude de projeção e probabilidade")
    ), "Distribua ⟨1| pelos dois termos.", "Use a ortogonalidade para zerar a contribuição com a.",
        "O produto ⟨0|ψ⟩, da mesma forma, dá a."),
    number("Qual é ⟨ψ|ψ⟩ para um estado normalizado?", "1",
        "O produto interno consigo mesmo é o quadrado da norma.", "Um estado normalizado tem norma 1, então o resultado é 1.",
        "Um vetor não normalizado (2, 0) tem produto interno consigo igual a 4."))

internal fun Curriculum.schrodingerTime() = lesson(
    "schrodinger-time", "Equação de Schrödinger dependente do tempo", "Identifique o que governa a evolução de um estado fechado.",
    "A equação de Schrödinger liga a mudança instantânea do estado ao Hamiltoniano, o operador que representa a energia do sistema.",
    "Um autoestado de energia ganha uma fase que gira com o tempo. Seu módulo e suas probabilidades permanecem constantes.",
    """i\hbar\frac{\partial\psi}{\partial t}=H\psi""",
    "i² = −1, ℏ é a constante de Planck reduzida, ψ é a função de onda, ∂ψ/∂t é sua derivada temporal e H é o Hamiltoniano.",
    "Para Hamiltoniano independente do tempo e autoestado de energia E, ψ(t) = exp(−iEt/ℏ)ψ(0). Esse fator tem módulo 1. Em superposições, diferentes energias acumulam fases relativas.",
    Diagram.Wave, "A curva ilustra a parte real de uma fase oscilatória; o módulo do exponencial complexo permanece unitário.",
    "O Hamiltoniano determina a derivada no tempo e as energias controlam as fases.",
    "O modelo descreve dinâmica coerente em átomos e portas quânticas idealizadas.",
    listOf(mit, nist), listOf("derivatives", "differential-equations", "eigenvalues", "dirac"),
    choose("Com Hamiltoniano constante, um autoestado de energia evolui de que forma?", 0, listOf(
        correct("Adquire apenas uma fase global", "O fator exp(−iEt/ℏ) multiplica o estado inteiro com módulo 1."),
        wrong("Sua norma decai com o tempo", "A fase tem expoente imaginário. A evolução do sistema fechado preserva a norma.", "Fase e decaimento"),
        wrong("Mistura todos os autoestados de energia", "O operador atua no próprio autoestado por um fator. A evolução permanece nessa direção.", "Autoestado e superposição")
    ), "Substitua Hψ = Eψ na equação de evolução.", "A solução é um fator de fase multiplicando o estado inicial.",
        "Uma soma de dois autoestados com energias diferentes pode mudar sua fase relativa."),
    number("Qual é o módulo do fator exp(−iEt/ℏ) para E e t reais?", "1",
        "O expoente é puramente imaginário.", "O exponencial de uma fase real vezes i tem módulo 1.",
        "O módulo de exp(iπ) é 1, embora exp(iπ) = −1."))

internal fun Curriculum.schrodingerStationary() = lesson(
    "schrodinger-stationary", "Equação de Schrödinger independente do tempo", "Reconheça a equação de autovalores da energia.",
    "Quando o Hamiltoniano é independente do tempo, os estados estacionários podem ser encontrados resolvendo a equação de autovalores e as condições de contorno.",
    "Em uma caixa de paredes infinitas, a função de onda deve ser zero nas paredes. Apenas modos com um número inteiro de meias-ondas cabem.",
    """H\varphi=E\varphi""",
    "H é o Hamiltoniano, φ é a parte espacial do autoestado e E é o autovalor de energia.",
    "Em uma dimensão, H inclui o termo cinético −ℏ²/(2m) vezes a segunda derivada espacial e o potencial V(x). A forma de V e as condições de contorno determinam quais E são permitidas.",
    Diagram.Levels, "Linhas horizontais indicam energias discretas possíveis de um sistema ligado.",
    "Procure funções que respeitem a fronteira e que voltem multiplicadas por E ao aplicar H.",
    "Resolver o problema estacionário permite prever energias de átomos e poços quânticos.",
    listOf(mit), listOf("schrodinger-time", "integrals"),
    choose("Em Hφ = Eφ, o que representa E?", 1, listOf(
        wrong("A função de onda", "A função é φ. E é o fator escalar resultante da aplicação de H.", "Autoestado e autovalor"),
        correct("A energia do autoestado", "H é o operador energia e E é o autovalor associado a φ."),
        wrong("A posição da partícula", "Posição é uma variável da função φ. E é uma grandeza com unidade de energia.", "Posição e energia")
    ), "Identifique H como operador energia.", "Leia o fator E que multiplica φ.",
        "Em um poço infinito de comprimento fixo, E₂ = 4E₁."),
    number("Uma caixa infinita tem Eₙ = n²E₁. Quanto vale a razão E₃/E₁?", "9",
        "Divida a expressão geral por E₁.", "Substitua n = 3 e obtenha 3² = 9.",
        "O quarto nível vale 16 vezes a energia do primeiro."))
