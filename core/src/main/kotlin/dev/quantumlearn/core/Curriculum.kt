package dev.quantumlearn.core

object Curriculum {
    val prealgebra = Reference("OpenStax · Prealgebra 2e", "https://openstax.org/details/books/prealgebra-2e")
    val algebra = Reference("OpenStax · Algebra and Trigonometry 2e", "https://openstax.org/details/books/algebra-and-trigonometry-2e")
    val statistics = Reference("OpenStax · Introductory Statistics 2e", "https://openstax.org/details/books/introductory-statistics-2e")
    val waves = Reference("OpenStax · University Physics, vol. 1, ondas", "https://openstax.org/books/university-physics-volume-1/pages/16-introduction")
    val photon = Reference("OpenStax · University Physics, vol. 3, fótons", "https://openstax.org/books/university-physics-volume-3/pages/6-2-photoelectric-effect")
    val nist = Reference("NIST · Constantes fundamentais CODATA", "https://physics.nist.gov/cuu/Constants/")
    val feynman = Reference("Caltech · The Feynman Lectures, III-1", "https://www.feynmanlectures.caltech.edu/III_01.html")
    val mit = Reference("MIT OpenCourseWare · 8.04 Quantum Physics I", "https://ocw.mit.edu/courses/8-04-quantum-physics-i-spring-2016/")
    val mitSpin = Reference("MIT OpenCourseWare · 8.05 Quantum Physics II", "https://ocw.mit.edu/courses/8-05-quantum-physics-ii-fall-2013/")

    val calculus = Reference("OpenStax · Calculus, vol. 1", "https://openstax.org/details/books/calculus-volume-1")
    val mechanics = Reference("OpenStax · University Physics, vol. 1", "https://openstax.org/details/books/university-physics-volume-1")
    val fields = Reference("OpenStax · University Physics, vol. 2", "https://openstax.org/details/books/university-physics-volume-2")
    val linear = Reference("MIT OpenCourseWare · 18.06 Linear Algebra", "https://ocw.mit.edu/courses/18-06-linear-algebra-spring-2010/")
    val differential = Reference("MIT OpenCourseWare · 18.03 Differential Equations", "https://ocw.mit.edu/courses/18-03-differential-equations-spring-2010/")
    val fourier = Reference("MIT OpenCourseWare · 18.103 Fourier Analysis", "https://ocw.mit.edu/courses/18-103-fourier-analysis-fall-2013/")
    val qft = Reference("MIT OpenCourseWare · 8.323 Relativistic Quantum Field Theory I", "https://ocw.mit.edu/courses/8-323-relativistic-quantum-field-theory-i-spring-2023/")

    val modules: List<StudyModule> by lazy {
        listOf(
            StudyModule("Matemática básica", listOf(
                StudyUnit("Números e padrões", listOf(operations(), fractions(), powers())),
                StudyUnit("Operações inversas e escala", listOf(roots(), equations(), scientific()))
            )),
            StudyModule("Matemática intermediária", listOf(
                StudyUnit("Expressões e funções", listOf(algebraLesson(), functions(), trigonometry(), logarithms())),
                StudyUnit("Direção e fase", listOf(vectors(), complex()))
            )),
            StudyModule("Matemática avançada", listOf(
                StudyUnit("Variação e acumulação", listOf(limits(), derivatives(), integrals(), probability())),
                StudyUnit("Espaços e frequências", listOf(linearAlgebra(), matrices(), differentialEquations(), fourierTransform()))
            )),
            StudyModule("Fundamentos de física", listOf(
                StudyUnit("Movimento e energia", listOf(motion(), forces(), energy(), momentum())),
                StudyUnit("Ondas e campos", listOf(waveMotion(), electricity(), magnetism(), electromagneticFields()))
            )),
            StudyModule("Introdução à física quântica", listOf(
                StudyUnit("Luz quantizada", listOf(blackbody(), photons(), photoelectricEffect())),
                StudyUnit("Ondas de matéria", listOf(duality(), doubleSlit(), uncertainty()))
            )),
            StudyModule("Mecânica quântica", listOf(
                StudyUnit("Amplitudes e medidas", listOf(waveFunction(), born(), superposition(), quantumStates())),
                StudyUnit("Operadores e evolução", listOf(operators(), eigenvalues(), diracNotation(), schrodingerTime(), schrodingerStationary()))
            )),
            StudyModule("Física quântica avançada", listOf(
                StudyUnit("Barreiras, spin e sistemas compostos", listOf(tunneling(), spin(), entanglement(), decoherence())),
                StudyUnit("Modelos e aplicações", listOf(oscillator(), hydrogen(), perturbation(), quantumFields(), qubit()))
            )),
            StudyModule("Inglês 1 · A base da frase", listOf(
                StudyUnit("Quem é e o que é", listOf(englishPronouns(), englishToBe(), englishToBeQuestions())),
                StudyUnit("De quem é e onde está", listOf(englishPossessives(), englishArticles(), englishThereIs()))
            ), Track.English),
            StudyModule("Inglês 2 · Ações no presente", listOf(
                StudyUnit("O que você faz", listOf(englishPresent(), englishDoDoes(), englishContinuous())),
                StudyUnit("Pessoas e capacidades", listOf(englishObjectPronouns(), englishCan()))
            ), Track.English),
            StudyModule("Inglês 3 · Perguntas, passado e futuro", listOf(
                StudyUnit("Fazer perguntas", listOf(englishWh(), englishTime())),
                StudyUnit("Ontem e amanhã", listOf(englishWasWere(), englishPast(), englishFuture()))
            ), Track.English),
            StudyModule("Inglês 4 · Palavras de ligação (avançado)", listOf(
                StudyUnit("Tempo e sequência", listOf(englishJust(), englishSequence())),
                StudyUnit("Ligar ideias", listOf(englishLinkers(), englishAddition())),
                StudyUnit("Falar como nativo", listOf(englishFillers(), englishApostrophe()))
            ), Track.English)
        ).also(QuestionBank::validate)
    }

    val lessons by lazy { modules.flatMap { it.lessons } }
    val questions by lazy { lessons.flatMap { it.questions } }
    val byId by lazy { lessons.associateBy(Lesson::id) }
    private val tracks by lazy { modules.flatMap { module -> module.lessons.map { it.id to module.track } }.toMap() }
    fun track(lessonId: String) = tracks[lessonId] ?: Track.Science
    val english = Reference("British Council · LearnEnglish Grammar", "https://learnenglish.britishcouncil.org/grammar")
    val cambridge = Reference("Cambridge Dictionary · English Grammar Today", "https://dictionary.cambridge.org/grammar/british-grammar/")

    internal data class Draft(
        val prompt: String,
        val answer: AnswerSpec,
        val steps: List<String>,
        val different: String,
        val difficulty: Difficulty
    )

    internal fun correct(label: String, why: String) = Option(label, why)
    internal fun wrong(label: String, why: String, confused: String, type: ErrorKind = ErrorKind.Interpretation) =
        Option(label, why, type, confused)

    internal fun choose(
        prompt: String, correct: Int, options: List<Option>, step1: String, step2: String, different: String,
        type: ChoiceType = ChoiceType.Multiple, difficulty: Difficulty = Difficulty.Introductory
    ) = Draft(prompt, AnswerSpec.Choice(options, correct, type), listOf(step1, step2), different, difficulty)

    internal fun number(
        prompt: String, result: String, step1: String, step2: String, different: String,
        unit: String? = null, mistakes: Map<String, Option> = emptyMap(),
        difficulty: Difficulty = Difficulty.Intermediate, relativeTolerance: Double = 1e-7,
        format: NumberFormat = NumberFormat.Value
    ) = Draft(prompt, AnswerSpec.Numeric(result, unit, relativeTolerance, mistakes = mistakes, format = format), listOf(step1, step2), different, difficulty)

    private fun written(
        prompt: String, model: String, accepted: Set<String>, clarification: String,
        step1: String, step2: String, different: String
    ) = Draft(prompt, AnswerSpec.Written(accepted, clarification, model), listOf(step1, step2), different, Difficulty.Advanced)

    internal fun lesson(
        id: String, title: String, objective: String, introduction: String, example: String,
        latex: String, symbols: String, detail: String,
        diagram: Diagram, caption: String, summary: String, application: String,
        sources: List<Reference>, prerequisites: List<String>, vararg drafts: Draft
    ): Lesson {
        val formula = Formula(latex, symbols)
        val ids = drafts.indices.map { "$id-${it + 1}" }
        val questions = drafts.mapIndexed { index, draft ->
            Question(ids[index], id, title, prerequisites, draft.difficulty, draft.prompt, draft.answer,
                draft.steps, draft.different, formula, sources, ids.filter { it != ids[index] })
        }
        return Lesson(id, title, objective, introduction, example, formula, detail, diagram, caption,
            summary, application, sources, prerequisites, questions)
    }

    private fun operations() = lesson(
        "operations", "Operações matemáticas", "Some e multiplique na ordem correta.",
        "Somar junta quantidades. Multiplicar conta grupos iguais. Quando as duas operações aparecem juntas, resolvemos a multiplicação antes da adição, salvo indicação de parênteses.",
        "Você tem 2 moedas soltas e 3 envelopes com 4 moedas em cada. Conte as moedas dos envelopes primeiro. São 12, mais as 2 soltas, totalizando 14.",
        """2+3\times4=14""",
        "2 conta as moedas soltas. 3 conta os envelopes. 4 é a quantidade por envelope. O símbolo × indica multiplicação.",
        "Parênteses mudam a estrutura. Em (2 + 3) × 4, temos cinco grupos de quatro e obtemos 20. O lugar em que agrupamos as parcelas muda o que a expressão representa.",
        Diagram.Groups, "Três grupos com quatro pontos cada, mais dois pontos soltos.",
        "Interprete parênteses primeiro. Multiplique ou divida antes de somar ou subtrair.",
        "A mesma ordem é necessária para substituir valores em fórmulas de energia e movimento.",
        listOf(prealgebra), emptyList(),
        choose("Quanto vale 2 + 3 × 4?", 1, listOf(
            wrong("20", "Você parece ter somado 2 e 3 antes de multiplicar. Esse seria o resultado de (2 + 3) × 4.", "Prioridade da multiplicação", ErrorKind.Calculation),
            correct("14", "Multiplique 3 por 4 e só então junte as 2 unidades."),
            wrong("9", "Somar os três números troca o produto 3 × 4 por uma soma. O símbolo × pede três grupos de quatro.", "Soma e multiplicação"),
            wrong("12", "O produto 3 × 4 vale 12. Ainda faltam as 2 unidades que estão fora dos grupos.", "Termo fora do produto", ErrorKind.Calculation)
        ), "Calcule 3 × 4 = 12.", "Some 2 + 12 = 14.", "Em 1 + 2 × 5, o produto vale 10 e o total vale 11."),
        number("Calcule (4 + 2) × 3.", "18", "Resolva 4 + 2 = 6 dentro dos parênteses.",
            "Multiplique 6 × 3 = 18.", "Em (5 + 1) × 2, obtemos 6 × 2 = 12.",
            mistakes = mapOf("10" to wrong("10", "O valor 10 vem de 4 + 2 × 3. Aqui, os parênteses pedem a soma antes do produto.",
                "Efeito dos parênteses", ErrorKind.Formula))),
        choose("Os parênteses fazem (1 + 2) × 5 valer 15.", 0, listOf(
            correct("Verdadeiro", "A soma agrupada vale 3 e 3 × 5 vale 15."),
            wrong("Falso", "Se você obteve 11, provavelmente multiplicou 2 × 5 antes de resolver os parênteses.", "Prioridade dos parênteses")
        ), "Resolva 1 + 2 = 3.", "Calcule 3 × 5 = 15.", "Em (2 + 2) × 3, são quatro grupos de três.", ChoiceType.TrueFalse)
    )

    private fun fractions() = lesson(
        "fractions", "Frações", "Represente partes iguais de um todo.",
        "Uma fração indica quantas partes iguais do todo estamos contando. O número de cima conta as partes escolhidas, e o de baixo diz em quantas partes iguais o todo foi dividido.",
        "Uma barra dividida em quatro pedaços iguais tem a mesma metade visível quando você escolhe dois pedaços. A fração 2/4 representa o mesmo valor que 1/2.",
        """\frac{2}{4}=\frac{1}{2}""",
        "2 e 1 são numeradores. 4 e 2 são denominadores. A barra também pode ser lida como uma divisão.",
        "Multiplicar ou dividir ambos os números da fração por um mesmo número não nulo preserva o valor. Dividir por zero é indefinido.",
        Diagram.Parts, "As duas primeiras de quatro partes têm a mesma extensão que metade da barra.",
        "Frações equivalentes representam a mesma quantidade. A escala do numerador e a do denominador precisam mudar juntas.",
        "Proporções e probabilidades usam a mesma ideia de parte em relação ao total.",
        listOf(prealgebra), listOf("operations"),
        choose("Qual fração é equivalente a 2/4?", 0, listOf(
            correct("1/2", "Dividir numerador e denominador por 2 mantém o valor."),
            wrong("2/2", "Apenas o denominador foi dividido. 2/2 cobre o inteiro e 2/4 cobre metade.", "Simplificação dos dois termos"),
            wrong("1/4", "Apenas o numerador foi dividido. 1/4 tem metade do valor de 2/4.", "Simplificação dos dois termos"),
            wrong("4/2", "Trocar numerador e denominador produz o inverso. 4/2 vale 2.", "Fração e seu inverso")
        ), "O numerador 2 dividido por 2 dá 1.", "O denominador 4 dividido por 2 dá 2.", "6/8 é 3/4 após dividir os dois termos por 2."),
        number("Escreva uma fração irredutível equivalente a 6/9.", "2/3",
            "Encontre o fator comum 3.", "Calcule 6 ÷ 3 = 2 e 9 ÷ 3 = 3.", "10/15 = 2/3 por divisão de ambos os termos por 5.",
            mistakes = mapOf("2/9" to wrong("2/9", "Foi dividido só o número de cima. Divida também 9 por 3.",
                "Simplificação dos dois termos", ErrorKind.Formula)), format = NumberFormat.ReducedFraction)
    )

    private fun powers() = lesson(
        "powers", "Potenciação", "Leia uma potência como produto repetido.",
        "Uma potência com expoente inteiro positivo repete o mesmo fator. O número de baixo é a base. O expoente diz quantas cópias da base entram na multiplicação.",
        "Uma construção com 3 pontos em cada lado de um quadrado contém 3 × 3, ou 3², pontos.",
        """3^{2}=3\times3=9""",
        "3 é a base e 2 é o expoente. × indica o produto dos dois fatores.",
        "Usando a mesma base, 3² × 3³ tem cinco fatores 3, então vale 3⁵. Uma base diferente muda os fatores e a regra exige atenção.",
        Diagram.Square, "Três linhas com três pontos em cada linha formam um quadrado com nove pontos.",
        "A base é o fator repetido. O expoente conta quantas vezes ele aparece.",
        "Em física quântica, elevar o módulo de uma amplitude ao quadrado produz uma probabilidade ou densidade.",
        listOf(prealgebra), listOf("operations"),
        choose("Que produto representa 3²?", 2, listOf(
            wrong("3 × 2", "O expoente 2 conta dois fatores 3. Ele não substitui nenhum dos fatores.", "Expoente e fator"),
            wrong("2 × 2 × 2", "Esse produto tem base 2 e expoente 3. Em 3², os papéis estão invertidos.", "Base e expoente"),
            correct("3 × 3", "O expoente 2 pede duas cópias da base 3."),
            wrong("3 + 3", "Essa soma vale 6. Uma potência repete a multiplicação da base.", "Soma e potência")
        ), "Identifique a base 3 e o expoente 2.", "Repita o fator 3 duas vezes, obtendo 9.", "4² = 4 × 4 = 16."),
        number("Calcule 2³ × 2².", "32", "Junte os três fatores 2 da primeira potência aos dois da segunda.",
            "Multiplique os cinco fatores para obter 2⁵ = 32.", "3² × 3¹ = 3³ = 27.",
            mistakes = mapOf("64" to wrong("64", "64 vem de 2⁶. Ao multiplicar potências de mesma base, conte os fatores somando 3 + 2.",
                "Produto de potências e potência de potência", ErrorKind.Formula)))
    )

    private fun roots() = lesson(
        "roots", "Raízes", "Use a raiz quadrada para recuperar uma magnitude.",
        "A raiz quadrada principal de um número não negativo é o número não negativo cujo quadrado recupera o original.",
        "Se um piso quadrado tem área de 25 m², cada lado mede 5 m, pois 5 m × 5 m cobre os 25 m².",
        """\sqrt{25}=5""",
        "O radical √ pede a raiz quadrada. 25 é o número sob o radical, e 5 é a raiz principal.",
        "A equação x² = 25 aceita 5 e −5. Já o símbolo √25 representa apenas 5. Essa diferença evita atribuir um sinal negativo a comprimentos e magnitudes.",
        Diagram.Square, "Conte três pontos em cada lado. A raiz quadrada de nove recupera três.",
        "Raiz quadrada é a inversa do quadrado quando começamos com valores não negativos.",
        "A raiz de uma soma de quadrados aparece ao calcular comprimentos e módulos de números complexos.",
        listOf(prealgebra), listOf("powers"),
        choose("Qual é o valor de √25?", 3, listOf(
            wrong("12,5", "Dividir por 2 dá 12,5, mas 12,5² não resulta em 25.", "Raiz e metade", ErrorKind.Formula),
            wrong("−5", "O quadrado de −5 também dá 25. O radical escolhe a raiz principal, que é não negativa.", "Raiz principal e soluções de uma equação"),
            wrong("625", "625 é o resultado de elevar 25 ao quadrado. A raiz desfaz essa operação no domínio não negativo.", "Quadrado e raiz"),
            correct("5", "5 é não negativo e 5 × 5 resulta em 25.")
        ), "Busque um número não negativo que multiplicado por si mesmo dê 25.",
            "Confira 5 × 5 = 25.", "√49 = 7 porque 7 × 7 = 49."),
        number("Calcule √81.", "9", "Procure um inteiro não negativo que tenha quadrado 81.",
            "Como 9² = 81, a raiz principal é 9.", "√64 = 8 pois 8² = 64.",
            mistakes = mapOf("-9" to wrong("−9", "−9 resolve x² = 81, mas a raiz principal é não negativa.",
                "Raiz principal e soluções de uma equação"))))
    
    private fun equations() = lesson(
        "equations", "Equações", "Isole o valor desconhecido preservando a igualdade.",
        "Uma equação diz que duas expressões têm o mesmo valor. Aplicar a mesma soma, subtração ou divisão por número não nulo aos dois lados preserva a igualdade.",
        "Uma balança equilibrada contém um pacote e 3 kg de um lado, e 7 kg do outro. Retire 3 kg de ambos os pratos e a massa do pacote fica isolada.",
        """x+3=7\quad\Rightarrow\quad x=4""",
        "x é o valor desconhecido, 3 é a parcela conhecida e 7 é o total. A seta apresenta a consequência algébrica.",
        "Na equação 2x + 1 = 9, faça a operação inversa de fora para dentro. Subtraia 1 de ambos os lados e então divida ambos por 2.",
        Diagram.Balance, "Os dois pratos da balança representam os dois lados da igualdade.",
        "Desfaça as operações na ordem inversa. Confira o resultado substituindo-o na equação original.",
        "Isolar grandezas permite calcular a frequência quando conhecemos a energia de um fóton.",
        listOf(prealgebra), listOf("operations"),
        Draft("Toque nas etapas na ordem que isola x em 2x + 1 = 9.",
            AnswerSpec.Sequence(listOf("Dividir ambos os lados por 2", "Subtrair 1 de ambos os lados"), listOf(1, 0),
                wrong("Dividir antes de subtrair", "Ao dividir primeiro, o termo constante passa a valer 1/2. Subtrair 1 em seguida não cancela essa parcela.",
                    "Operação inversa", ErrorKind.Formula)),
            listOf("Subtraia 1 em ambos os lados. Resta 2x = 8.", "Divida ambos por 2. Resta x = 4."),
            "Em 3x + 2 = 11, faça 3x = 9 e depois x = 3.", Difficulty.Introductory),
        number("Em 3x − 2 = 13, qual é o valor de x?", "5", "Some 2 dos dois lados, obtendo 3x = 15.",
            "Divida por 3. Confira que 3 × 5 − 2 = 13.", "Em 4x − 1 = 7, obtemos 4x = 8 e x = 2.",
            mistakes = mapOf("11/3" to wrong("11/3", "O 11 aparece ao subtrair 2. Aqui é preciso somar 2 para cancelar −2.",
                "Operação inversa", ErrorKind.Formula)))
    )

    private fun scientific() = lesson(
        "scientific", "Notação científica", "Escreva números grandes e pequenos como potências de dez.",
        "Para escrever um número positivo em notação científica, separe a parte entre 1 (inclusive) e 10 (exclusive) e uma potência inteira de dez.",
        "O número 0,004 corresponde a quatro milésimos. A forma 4 × 10⁻³ ajuda a enxergar a escala sem contar zeros repetidamente.",
        """0{,}004=4\times10^{-3}""",
        "4 é o coeficiente. 10 é a base. O expoente −3 indica divisão por 10³.",
        "Ao passar de 0,004 para o coeficiente 4, a vírgula anda três casas para a direita. Multiplicar por 10⁻³ desfaz esse aumento.",
        Diagram.Scale, "Cada passo à direita na escala multiplica o valor por dez.",
        "Expoentes negativos representam fatores menores que um. O coeficiente normalizado fica entre 1 e 10.",
        "Energias de fótons em joules ficam muitas ordens de grandeza abaixo de 1 J.",
        listOf(prealgebra), listOf("powers", "fractions"),
        choose("Qual é a forma normalizada de 0,004?", 2, listOf(
            wrong("4 × 10³", "10³ multiplica o 4 por mil e produz 4000. Para um número abaixo de 1, use o expoente negativo.", "Sinal do expoente"),
            wrong("4 × 10⁻⁴", "Dividir 4 por 10 000 produz 0,0004, dez vezes menor.", "Contagem das casas", ErrorKind.Calculation),
            correct("4 × 10⁻³", "4 dividido por 1000 resulta em 0,004."),
            wrong("0,4 × 10⁻²", "O valor está correto, mas 0,4 é menor que 1. A forma normalizada usa um coeficiente a partir de 1.", "Normalização do coeficiente")
        ), "Desloque a vírgula três casas para formar 4.", "Multiplique 4 por 10⁻³ para voltar à escala inicial.",
            "0,052 em notação normalizada é 5,2 × 10⁻²."),
        number("Escreva o valor 0,00032 em notação científica, por exemplo 2 × 10^3.", "3.2e-4",
            "O coeficiente normalizado é 3,2.", "Use 10⁻⁴ para compensar as quatro casas.",
            "0,00091 equivale a 9,1 × 10⁻⁴.",
            mistakes = mapOf("3.2e4" to wrong("3,2 × 10⁴", "Use expoente negativo para dividir por dez mil.",
                "Sinal do expoente", ErrorKind.Formula)), format = NumberFormat.Scientific)
    )

    private fun functions() = lesson(
        "functions", "Funções", "Vincule cada entrada a uma única saída.",
        "Uma função associa a cada entrada permitida exatamente uma saída. O gráfico reúne os pares de entrada e saída.",
        "Um estacionamento cobra 4 reais fixos e 2 por hora. Após três horas, o custo é 10 reais.",
        """f(x)=2x+4""",
        "x é a entrada em horas e f(x) é o custo em reais. 2 é a tarifa por hora, e 4, a parcela fixa.",
        "O gráfico da função é uma reta. A cada hora extra, o custo aumenta 2 reais. A altura em x = 0 representa a parcela fixa.",
        Diagram.Line, "A reta cruza o eixo vertical acima de zero e sobe conforme x aumenta.",
        "Para avaliar uma função, substitua a entrada na regra e calcule a saída.",
        "Uma função de onda atribui uma amplitude a cada posição do espaço, em um instante dado.",
        listOf(algebra), listOf("equations"),
        choose("Que gráfico descreve f(x) = 2x + 4 para x ≥ 0?", 1, listOf(
            wrong("Uma reta decrescente que começa em 4", "O coeficiente de x vale +2, então cada passo à direita aumenta f(x).", "Sinal da inclinação"),
            correct("Uma reta crescente que começa em 4", "A inclinação é positiva e f(0) = 4."),
            wrong("Uma reta crescente que começa em 0", "Quando x é zero, 2x desaparece. A parcela 4 permanece.", "Intercepto vertical")
        ), "Calcule f(0) = 4.", "Calcule f(1) = 6. A subida confirma a inclinação positiva.",
            "Se g(x) = 3x + 1, a reta começa em 1 e ganha 3 por passo.", ChoiceType.Graph),
        number("Qual é f(3) para f(x) = 2x + 4?", "10", "Substitua x por 3, obtendo 2 × 3 + 4.",
            "Multiplique 2 × 3 e some 4. O resultado é 10.", "Para x = 5, o valor é 2 × 5 + 4 = 14."))
    
    private fun complex() = lesson(
        "complex", "Números complexos", "Calcule a intensidade de um número no plano complexo.",
        "Um número complexo é uma soma de uma parte real e uma parte imaginária. Podemos desenhá-lo como um ponto no plano. Seu módulo é a distância até a origem.",
        "Imagine caminhar três passos para leste e quatro para norte. A distância em linha reta desde a origem é cinco passos.",
        """z=a+bi\quad |z|^{2}=a^{2}+b^{2}""",
        "z é o número complexo, a é a parte real, b é a parte imaginária e i² = −1. |z| é o módulo.",
        "O quadrado do módulo pode ser obtido multiplicando z por seu conjugado, a − bi. Os termos cruzados se cancelam e i² = −1 torna a soma dos quadrados positiva.",
        Diagram.Vector, "O vetor do ponto (3, 4) tem componentes perpendiculares e comprimento 5.",
        "Some os quadrados das partes real e imaginária para obter o quadrado do módulo.",
        "Amplitudes quânticas podem ser complexas. Para calcular probabilidades, usamos o quadrado de seus módulos.",
        listOf(algebra, mit), listOf("roots"),
        choose("Qual é |3 + 4i|²?", 2, listOf(
            wrong("7", "Somar 3 e 4 seria somar componentes ao longo de eixos diferentes. A distância usa quadrados.", "Componentes e comprimento", ErrorKind.Formula),
            wrong("−7", "O −7 é a parte real de (3 + 4i)². O quadrado do módulo é real e não negativo.", "Quadrado complexo e módulo", ErrorKind.Formula),
            correct("25", "As partes 3 e 4 contribuem com 9 e 16."),
            wrong("5", "5 é o módulo. A pergunta pede o seu quadrado.", "Módulo e quadrado do módulo")
        ), "Leia as componentes 3 e 4.", "Some 3² + 4² = 9 + 16 = 25.", "Para z = 1 + 2i, |z|² = 5."),
        number("Calcule |5 + 12i|.", "13", "O quadrado do módulo é 25 + 144 = 169.",
            "Tire a raiz quadrada principal. √169 = 13.", "O módulo de 8 + 15i é 17.",
            mistakes = mapOf("169" to wrong("169", "169 é a distância ao quadrado. Extraia sua raiz para obter a distância.",
                "Módulo e quadrado do módulo"))))
    
    private fun probability() = lesson(
        "probability", "Probabilidade", "Calcule a chance de um evento com resultados equiprováveis.",
        "A probabilidade de um evento vai de zero a um. Se todos os resultados têm chances iguais, divida a quantidade de resultados favoráveis pela quantidade total.",
        "Ao lançar um dado justo, três das seis faces têm valor par. A chance de sair par é 3/6, ou 1/2.",
        """P(A)=\frac{n_{A}}{n}""",
        "P(A) é a probabilidade do evento A, nₐ é a contagem favorável e n é a quantidade total de resultados equiprováveis.",
        "A regra vale com resultados equiprováveis. Se um dado for viciado, some as probabilidades das faces pares em vez de contar as faces.",
        Diagram.Probability, "As seis barras de igual altura representam faces equiprováveis. Três são destacadas.",
        "Identifique o espaço de resultados e as condições antes de calcular.",
        "Em medidas quânticas, resultados possíveis podem ter probabilidades diferentes, calculadas a partir do estado.",
        listOf(statistics), listOf("fractions"),
        choose("Um dado justo é lançado. Qual é a chance de obter 1 ou 2?", 2, listOf(
            wrong("1/6", "Cada uma das faces tem chance 1/6. O evento aceita qualquer uma das duas.", "União de eventos exclusivos"),
            wrong("2/4", "O denominador inclui as seis faces, inclusive aquelas que não satisfazem o evento.", "Espaço amostral"),
            correct("2/6", "Duas das seis faces contam para o evento."),
            wrong("4/6", "Quatro faces formam o evento complementar, sair 3, 4, 5 ou 6.", "Evento e complemento")
        ), "Conte 1 e 2 como resultados favoráveis.", "Divida 2 pelas 6 faces.", "No mesmo dado, a chance de sair 1, 2 ou 3 é 1/2."),
        number("Uma caixa tem 2 fichas verdes e 3 roxas. Com sorteio uniforme, qual é a probabilidade de uma ficha verde?", "2/5",
            "Conte as 2 fichas favoráveis.", "Divida pelas 5 fichas da caixa.", "Com 1 ficha verde entre 4, a chance é 1/4."))
    
    private fun waveMotion() = lesson(
        "waves", "Ondas", "Relacione comprimento de onda, frequência e velocidade.",
        "Uma onda repete um padrão enquanto se propaga. O comprimento de onda é a distância entre duas cristas consecutivas. Frequência é a quantidade de ciclos por segundo.",
        "Se chegam 2 cristas por segundo e as cristas estão 3 metros afastadas, o padrão avança 6 metros por segundo.",
        """v=\lambda f""",
        "v é a velocidade de propagação em m/s. λ é o comprimento de onda em metros. f é a frequência em hertz.",
        "Em um meio no qual a velocidade permanece constante, dobrar a frequência diminui à metade o comprimento de onda.",
        Diagram.Wave, "O comprimento de onda é medido horizontalmente entre duas cristas.",
        "Multiplique os metros por ciclo pelos ciclos por segundo para obter metros por segundo.",
        "A relação entre frequência e comprimento de onda conecta a cor da luz à energia de cada fóton.",
        listOf(waves), listOf("functions"),
        choose("Uma onda mantém a velocidade. O que acontece com λ se f dobra?", 1, listOf(
            wrong("Dobra", "Se os dois fatores dobrassem, o produto λf seria quatro vezes maior.", "Proporcionalidade inversa"),
            correct("Cai à metade", "Quando f dobra, λ deve cair à metade para conservar o produto."),
            wrong("Fica igual", "Com λ constante, dobrar f duplicaria a velocidade.", "Relação entre frequência e comprimento")
        ), "Reescreva a relação como λ = v/f.", "Se v é fixo e o denominador dobra, λ cai à metade.",
            "Com v = 6 m/s, passar de 2 Hz para 3 Hz muda λ de 3 m para 2 m."),
        number("Calcule a velocidade de uma onda com λ = 2 m e f = 4 Hz. Inclua a unidade.", "8",
            "Substitua em v = λf.", "Multiplique 2 m por 4 ciclos por segundo, obtendo 8 m/s.",
            "Com λ = 3 m e f = 3 Hz, v vale 9 m/s.", "m/s",
            mapOf("0.5" to wrong("0,5", "0,5 vem de 2 ÷ 4. Velocidade resulta de comprimento vezes frequência.",
                "Relação entre frequência e comprimento", ErrorKind.Formula))))
    
    private fun photons() = lesson(
        "photons", "Fótons e constante de Planck", "Calcule a energia de um fóton pela frequência.",
        "A luz pode trocar energia em porções chamadas fótons. Cada fóton de uma dada frequência carrega uma energia determinada pela constante de Planck.",
        "Um feixe azul e um feixe vermelho podem ter a mesma potência total. Um fóton azul tem frequência maior e, portanto, mais energia que um fóton vermelho.",
        """E=hf\quad h=6{,}62607015\times10^{-34}""",
        "E é a energia de um fóton em joules. f é a frequência em hertz. h é a constante de Planck em joule-segundo e tem valor exato no SI.",
        "Aumentar a intensidade de luz monocromática pode aumentar quantos fótons chegam por segundo. Para aumentar a energia individual, é necessário aumentar a frequência.",
        Diagram.Photon, "A onda com mais oscilações no mesmo intervalo representa a frequência mais alta.",
        "Energia por fóton cresce com a frequência. Energia total também depende da quantidade de fótons.",
        "Sensores ópticos e o efeito fotoelétrico dependem da troca de energia entre fótons e matéria.",
        listOf(photon, nist), listOf("blackbody", "scientific", "waves"),
        choose("Como dobrar a energia de cada fóton de um feixe monocromático?", 1, listOf(
            wrong("Dobrar a quantidade de fótons", "Mais fótons elevam a energia total. Cada fóton mantém E = hf para a mesma frequência.", "Energia total e por fóton"),
            correct("Dobrar a frequência", "Como h é constante, E dobra junto com f."),
            wrong("Reduzir a frequência à metade", "Reduzir f reduz E na mesma proporção.", "Proporcionalidade direta"),
            wrong("Dobrar o comprimento de onda no vácuo", "Um λ maior no vácuo significa f menor, pois c = λf.", "Frequência e comprimento de onda")
        ), "Identifique h como constante.", "Substitua 2f na fórmula, obtendo 2hf.",
            "Um fóton de 6 × 10¹⁴ Hz tem duas vezes a energia de um de 3 × 10¹⁴ Hz."),
        number("Um fóton tem f = 5 × 10¹⁴ Hz. Calcule a energia usando h = 6,62607015 × 10⁻³⁴ J·s. Inclua a unidade ou use h × 5e14 J.", "h*5e14",
            "Multiplique os coeficientes 6,62607015 e 5.", "Some os expoentes −34 e 14 e normalize. O resultado é 3,313035075 × 10⁻¹⁹ J.",
            "Em 10¹⁴ Hz, a energia é 6,62607015 × 10⁻²⁰ J.", "J", difficulty = Difficulty.Advanced, relativeTolerance = 0.005))
    
    private fun doubleSlit() = lesson(
        "double-slit", "Experimento da dupla fenda", "Preveja quando duas contribuições interferem.",
        "Com uma fonte coerente e duas fendas indistinguíveis, eventos individuais se acumulam em franjas claras e escuras. As amplitudes dos caminhos são somadas antes do cálculo de probabilidade.",
        "Duas ondulações na água podem chegar juntas com cristas alinhadas e se reforçar. Uma crista que chega junto de um vale de amplitude igual pode ser cancelada.",
        """P\propto|a+b|^{2}""",
        "P é a probabilidade de detecção em uma região pequena. a e b são amplitudes dos dois caminhos. ∝ indica proporcionalidade sujeita à normalização.",
        "Se cada caminho se correlaciona com um marcador ortogonal distinguível, as franjas desaparecem na distribuição que ignora o marcador. O padrão ideal pressupõe coerência e ausência dessa distinção.",
        Diagram.Interference, "No padrão ideal, franjas mais claras correspondem a maior probabilidade de detecção.",
        "A fase relativa entre os caminhos determina onde as amplitudes se reforçam ou se cancelam.",
        "Interferômetros exploram deslocamentos nas franjas para medir pequenas mudanças de distância ou fase.",
        listOf(feynman, mit), listOf("duality", "waves", "probability"),
        choose("Em certo ponto, as amplitudes são a e −a. Qual é o efeito ideal?", 0, listOf(
            correct("Cancelamento", "A soma a + (−a) é zero antes do quadrado do módulo."),
            wrong("A probabilidade dobra", "Isso vem de somar probabilidades independentes. Para caminhos coerentes indistinguíveis, some as amplitudes primeiro.", "Amplitudes e probabilidades"),
            wrong("O módulo quadrado quadruplica", "Isso ocorre quando ambas as amplitudes são a. Aqui elas têm sinais opostos.", "Fase relativa")
        ), "Some a e −a.", "Eleve o módulo de zero ao quadrado: zero.", "Duas amplitudes a alinhadas somam 2a e dão |2a|² = 4|a|²."),
        written("Sem informação de caminho e com fonte coerente, o que se deve somar primeiro para explicar as franjas?",
            "Somam-se as amplitudes dos caminhos.", setOf("As amplitudes", "As amplitudes dos caminhos", "As amplitudes das fendas",
                "Somar as amplitudes", "Somam-se as amplitudes", "As amplitudes de probabilidade"),
            "Especifique qual grandeza é somada e de onde vêm as duas contribuições.",
            "Identifique uma amplitude para cada caminho.", "Some essas amplitudes e calcule o quadrado do módulo.",
            "Amplitudes de magnitudes iguais e fase oposta geram um mínimo na distribuição ideal."))
    
    private fun waveFunction() = lesson(
        "wave-function", "Função de onda", "Diferencie amplitude complexa e densidade de probabilidade.",
        "A função de onda de uma partícula atribui uma amplitude a cada posição em um instante. O quadrado do módulo é uma densidade de probabilidade para a posição.",
        "Em um mapa de chuva, regiões com intensidade maior recebem mais água por unidade de área. Uma densidade de probabilidade indica quanto uma pequena região contribui para a chance total.",
        """\rho(x)=|\psi(x)|^{2}""",
        "x é a posição em um instante fixo, ψ é a amplitude complexa e ρ é a densidade de probabilidade por unidade de comprimento.",
        "Em posição contínua, some a densidade ao longo de um intervalo por integração. Uma função de onda normalizada tem integral de |ψ|² igual a 1 em todo o espaço.",
        Diagram.Wave, "Uma amplitude pode ser negativa no caso real. Seu módulo quadrado é sempre não negativo.",
        "A amplitude contém fase. A densidade é real, não negativa e depende do módulo quadrado.",
        "Em poços quânticos, a distribuição de posições esperada depende da função de onda do estado.",
        listOf(mit), listOf("functions", "complex", "probability", "integrals", "double-slit"),
        choose("Se ψ(x) = −0,3 em unidades compatíveis, qual é a densidade naquele ponto?", 2, listOf(
            wrong("−0,3", "−0,3 pode ser uma amplitude. A densidade usa o módulo ao quadrado e não pode ser negativa.", "Amplitude e densidade"),
            wrong("0,3", "0,3 é o módulo. Ele ainda precisa ser elevado ao quadrado.", "Módulo e quadrado do módulo", ErrorKind.Formula),
            correct("0,09", "O módulo quadrado de −0,3 vale 0,09."),
            wrong("−0,09", "Multiplicar dois valores negativos produz um valor positivo. O módulo já é não negativo.", "Sinal no quadrado", ErrorKind.Calculation)
        ), "Calcule o módulo |−0,3| = 0,3.", "Eleve 0,3 ao quadrado para obter 0,09.",
            "Uma amplitude real de −0,5 dá densidade 0,25 nas unidades correspondentes."),
        number("Uma amplitude local vale 0,3 + 0,4i em unidades compatíveis. Qual é o valor numérico de |ψ|²?", "0.25",
            "Calcule 0,3² = 0,09 e 0,4² = 0,16.", "Some 0,09 e 0,16.",
            "Se a amplitude vale 0,6 + 0,8i, o módulo quadrado vale 1."))
    
    private fun born() = lesson(
        "born", "Regra de Born", "Converta amplitudes normalizadas em probabilidades de medida.",
        "Em uma medida de resultados discretos, a amplitude associada a um resultado tem módulo quadrado igual à sua probabilidade.",
        "Um sistema com dois resultados pode ter probabilidades 1/4 e 3/4. As amplitudes correspondentes não precisam ter esses valores, pois serão elevadas ao quadrado em módulo.",
        """P_{j}=|a_{j}|^{2}""",
        "j identifica um resultado de uma medida em uma base ortonormal, a ⱼ é sua amplitude e P ⱼ é a probabilidade.",
        "Para todos os resultados da base, a soma dos módulos quadrados é 1. Multiplicar todas as amplitudes pela mesma fase preserva as probabilidades.",
        Diagram.Parts, "Uma parte marcada de quatro representa 25%, e as três restantes, 75%.",
        "Use o módulo quadrado de cada amplitude. Confira a normalização quando conhecer todos os resultados.",
        "A regra de Born liga o estado quântico às frequências observadas ao repetir a preparação e a medida.",
        listOf(mit), listOf("wave-function"),
        choose("Um resultado tem amplitude −1/2 num estado normalizado. Qual é a sua probabilidade?", 2, listOf(
            wrong("−1/2", "A amplitude pode ter sinal. O módulo quadrado é uma probabilidade real não negativa.", "Amplitude e probabilidade"),
            wrong("1/2", "1/2 é o módulo. A regra de Born pede o seu quadrado.", "Módulo e quadrado do módulo", ErrorKind.Formula),
            correct("1/4", "|−1/2|² = 1/4."),
            wrong("3/4", "3/4 é a probabilidade total dos demais resultados.", "Resultado e complemento")
        ), "Encontre o módulo 1/2.", "Calcule (1/2)² = 1/4.", "Uma amplitude de √3/2 produz probabilidade 3/4."),
        number("Dois resultados esgotam a medida. O primeiro tem amplitude 1/2. Qual é a probabilidade do segundo?", "3/4",
            "Use a regra de Born para obter 1/4 para o primeiro.", "Subtraia 1/4 da probabilidade total 1.",
            "Se um resultado tem probabilidade 0,2, o único resultado restante tem 0,8."))
    
    private fun spin() = lesson(
        "spin", "Spin", "Preveja uma medida de spin em dois eixos.",
        "Spin é um momento angular intrínseco. Para uma partícula de spin 1/2, uma medida de componente em um eixo pode resultar em +ℏ/2 ou −ℏ/2.",
        "Se a partícula está preparada no estado positivo de z, medir em z dá +ℏ/2 com certeza. Medir em x, perpendicular a z, dá 50% para cada sinal.",
        """S_{z}=\pm\frac{\hbar}{2}""",
        "S z indica um possível valor de medida da componente em z. ℏ é a constante de Planck reduzida. ± representa os dois resultados possíveis.",
        "Para um estado puro com spin positivo em uma direção, a chance de medir o sinal positivo em um eixo separado por ângulo θ é cos²(θ/2). Em eixos perpendiculares, θ é 90°.",
        Diagram.Vector, "Os eixos horizontal e vertical representam direções de medida perpendiculares.",
        "A direção da preparação e o eixo de medida determinam as probabilidades.",
        "Experimentos do tipo Stern–Gerlach separam feixes de acordo com a projeção do momento magnético.",
        listOf(mitSpin, nist), listOf("born"),
        choose("Uma partícula de spin 1/2 está no estado positivo de z. O que se prevê para uma medida ideal em x?", 2, listOf(
            wrong("+ℏ/2 com certeza", "A certeza se aplica a repetir a medida em z. Um eixo perpendicular define uma base diferente.", "Eixo de preparação e medida"),
            wrong("Zero com certeza", "Uma média de zero é compatível com resultados opostos. Zero não é um autovalor de Sx para spin 1/2.", "Valor esperado e resultado individual"),
            correct("50% para +ℏ/2 e 50% para −ℏ/2", "A preparação tem projeções de módulos iguais na base de x.")
        ), "x é perpendicular a z, então θ = 90°.", "A probabilidade positiva é cos²(45°) = 1/2 e a outra completa 1.",
            "Medir no sentido oposto a z dá o sinal negativo com certeza."),
        number("Em medidas de spin no eixo x sobre preparações idênticas positivas em z, qual é a probabilidade do sinal negativo?", "1/2",
            "O ângulo entre os eixos é 90°, então cos²(45°) = 1/2.", "O complemento da probabilidade positiva é 1 − 1/2 = 1/2.",
            "Para eixo igual ao de preparação, a chance do sinal negativo é zero."))
    
    private fun qubit() = lesson(
        "qubit", "Computação quântica e qubits", "Leia as probabilidades de um qubit na base computacional.",
        "Um qubit puro é descrito por duas amplitudes complexas normalizadas. A medida na base computacional produz 0 ou 1.",
        "Um qubit preparado com amplitudes 1/√2 e 1/√2 dá metade de cada resultado em muitas preparações repetidas.",
        """|\psi\rangle=a|0\rangle+b|1\rangle""",
        "O ket |ψ⟩ nomeia o estado. |0⟩ e |1⟩ são estados da base computacional e a e b são amplitudes, com |a|² + |b|² = 1.",
        "1/√2 e −1/√2 também dão 50% de cada resultado nessa base. A inversão de sinal muda a fase relativa e pode ser distinguida ao medir em outra base.",
        Diagram.Parts, "Metade da barra indica a probabilidade de 0; a metade restante, a de 1.",
        "Um qubit puro exige amplitudes normalizadas. A fase relativa pode alterar medidas em outras bases.",
        "Computação quântica usa portas para mudar amplitudes e fases antes da leitura da base de saída.",
        listOf(mit, mitSpin), listOf("born", "spin", "operators"),
        Draft("Associe cada grandeza à sua interpretação no estado a|0⟩ + b|1⟩.",
            AnswerSpec.Matching(listOf(
                Relationship("a", "Amplitude de |0⟩", "O coeficiente a acompanha |0⟩."),
                Relationship("b", "Amplitude de |1⟩", "O coeficiente b acompanha |1⟩."),
                Relationship("|b|²", "Probabilidade de medir 1", "A regra de Born usa o módulo quadrado de b para a chance de medir 1.")
            )),
            listOf("Identifique a e b como os coeficientes dos estados |0⟩ e |1⟩.",
                "Calcule |b|² para obter a probabilidade do resultado 1."),
            "Para a = √3/2 e b = 1/2, a chance de 1 é 1/4.", Difficulty.Introductory),
        number("Para a = 3/5 e b = 4/5, qual é a probabilidade de medir 1?", "16/25",
            "Selecione a amplitude b = 4/5.", "Eleve o módulo ao quadrado, resultando em 16/25.",
            "Se b = i/2 em um estado normalizado, a chance de 1 é 1/4."))
}
