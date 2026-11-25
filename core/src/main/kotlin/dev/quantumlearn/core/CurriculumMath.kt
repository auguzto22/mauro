package dev.quantumlearn.core

internal fun Curriculum.algebraLesson() = lesson(
    "algebra", "Álgebra", "Simplifique expressões reunindo termos semelhantes.",
    "Letras representam valores. Termos com a mesma parte literal podem ser somados, da mesma forma que três caixas de um tipo e duas do mesmo tipo viram cinco caixas.",
    "Se cada ingresso custa x reais, comprar três pela manhã e dois à tarde custa 5x reais.",
    "3x+2x=5x", "x é o preço por ingresso. 3 e 2 são as quantidades compradas.",
    "Pela distributividade, ax + bx = (a + b)x. Um termo constante tem parte literal 1. Por isso x e 3 não se combinam em um único termo semelhante.",
    Diagram.Groups, "Três grupos de quatro unidades mais duas unidades ilustram quantidades que podem ser contadas em conjunto.",
    "Separe os termos pela parte literal e some os coeficientes dos termos semelhantes.",
    "Simplificar equações antes de substituir valores reduz erros de cálculo.",
    listOf(algebra), listOf("equations"),
    choose("Que expressão equivale a 4x + x?", 1, listOf(
        wrong("4x²", "x² surgiria se você multiplicasse x por x. Aqui os termos são somados.", "Soma e produto"),
        correct("5x", "x tem coeficiente 1, então 4 + 1 = 5."),
        wrong("4 + x", "O primeiro 4 multiplica x. Ele não pode ser separado do termo 4x.", "Coeficiente e termo constante")
    ), "Escreva x como 1x.", "Some os coeficientes, obtendo 5x.", "2a + 7a = 9a."),
    number("Se x = 3, quanto vale 2x + 4x?", "18", "Some os coeficientes para obter 6x.",
        "Substitua x por 3. O resultado é 6 × 3 = 18.", "Se x = 5, x + 2x = 15."))

internal fun Curriculum.trigonometry() = lesson(
    "trigonometry", "Trigonometria", "Relacione os ângulos de um triângulo às razões entre seus lados.",
    "Em um triângulo retângulo, o seno de um ângulo agudo é o cateto oposto dividido pela hipotenusa. O cosseno usa o cateto adjacente.",
    "Uma rampa de 10 metros a 30° do chão sobe 5 metros, pois sen 30° = 1/2.",
    """\sin \theta=\frac{o}{h}""",
    "θ é o ângulo agudo, o é o comprimento do cateto oposto e h é a hipotenusa.",
    "Triângulos semelhantes mantêm as razões de lados ao mudar de tamanho. Radianos estendem essas funções ao círculo e são usados em expressões de fase de ondas.",
    Diagram.Vector, "No triângulo desenhado, o lado inclinado é a hipotenusa e os lados perpendicular e horizontal são os catetos.",
    "Para o seno, use o lado oposto ao ângulo. Para o cosseno, use o adjacente.",
    "Decompor vetores e prever interferência exige conhecer seno e cosseno.",
    listOf(algebra), listOf("fractions", "functions"),
    choose("Qual razão define o seno de um ângulo agudo?", 0, listOf(
        correct("Oposto / hipotenusa", "O seno mede a contribuição perpendicular à direção adjacente."),
        wrong("Adjacente / hipotenusa", "Essa razão define o cosseno do ângulo.", "Seno e cosseno"),
        wrong("Oposto / adjacente", "Essa razão define a tangente.", "Seno e tangente")
    ), "Localize o lado oposto ao ângulo.", "Divida seu comprimento pela hipotenusa.", "No triângulo 3, 4, 5, o seno do ângulo oposto ao lado 3 é 3/5."),
    number("Uma escada mede 8 m e forma 30° com o solo. Qual é sua altura? Use sen 30° = 1/2 e inclua a unidade.",
        "4", "Multiplique a hipotenusa pelo seno do ângulo.", "8 m × 1/2 = 4 m.", "Com 6 m no mesmo ângulo, a altura é 3 m.", "m"))

internal fun Curriculum.logarithms() = lesson(
    "logarithms", "Logaritmos", "Recupere o expoente de uma potência.",
    "Um logaritmo pergunta qual expoente devemos aplicar a uma base para obter um número. Em números reais, a base é positiva e diferente de 1 e o argumento é positivo.",
    "Se uma quantidade dobra a cada rodada, alcançar oito vezes a quantidade inicial exige três rodadas. Em base dois, o logaritmo de oito é três.",
    """\log_{2}(8)=3\quad\Rightarrow\quad 2^{3}=8""",
    "2 é a base, 8 é o argumento e 3 é o expoente procurado.",
    "Na base 10, cada fator de 10 acrescenta uma unidade ao logaritmo. A identidade log(ab) = log(a) + log(b) usa a mesma base e argumentos positivos.",
    Diagram.Scale, "Passos iguais numa escala logarítmica correspondem ao mesmo fator multiplicativo.",
    "Traduza logaritmos em potências para conferir o resultado.",
    "Escalas logarítmicas permitem comparar intensidades e processos de decaimento.",
    listOf(algebra), listOf("powers", "functions"),
    choose("Qual potência resolve log₃(81)?", 2, listOf(
        wrong("3³ = 27", "27 é uma potência de 3, mas o argumento solicitado é 81.", "Argumento do logaritmo", ErrorKind.Calculation),
        wrong("81³", "A base é 3, e 81 é o resultado da potência.", "Base e argumento"),
        correct("3⁴ = 81", "O expoente 4 é exatamente o valor do logaritmo.")
    ), "Busque um expoente e com 3ᵉ = 81.", "Como 3 × 3 × 3 × 3 = 81, e = 4.", "log₅(25) = 2."),
    number("Qual é log₁₀(1000)?", "3", "Escreva 1000 como potência de 10.",
        "Três fatores 10 produzem 1000.", "log₁₀(0,01) = −2."))

internal fun Curriculum.vectors() = lesson(
    "vectors", "Vetores", "Some vetores por componentes.",
    "Um vetor pode representar deslocamento com direção e sentido. Em um plano, descrevemos o vetor por duas projeções sobre eixos perpendiculares.",
    "Caminhar dois passos a leste e um ao norte pode ser escrito (2, 1). Caminhar mais três ao norte leva a (2, 4).",
    "(2,1)+(0,3)=(2,4)",
    "Cada par indica a componente horizontal e a vertical, nessa ordem.",
    "Some horizontal com horizontal e vertical com vertical. O comprimento de (x, y) é √(x² + y²), uma grandeza diferente da soma x + y.",
    Diagram.Vector, "O segmento inclinado tem uma componente horizontal e uma vertical.",
    "Compare e some componentes sobre o mesmo eixo.",
    "Forças e campos eletromagnéticos usam vetores. Estados quânticos usam uma generalização com componentes complexas.",
    listOf(algebra, linear), listOf("roots", "trigonometry"),
    choose("Quanto vale (2, 1) + (0, 3)?", 1, listOf(
        wrong("(3, 3)", "Some componentes correspondentes. A soma 2 + 1 mistura os dois eixos do primeiro vetor.", "Correspondência entre eixos"),
        correct("(2, 4)", "As somas horizontal e vertical são 2 + 0 e 1 + 3."),
        wrong("(2, 3)", "Falta somar a componente vertical 1 do primeiro vetor.", "Contribuições no eixo vertical", ErrorKind.Calculation)
    ), "Some 2 + 0 no eixo horizontal.", "Some 1 + 3 no eixo vertical.", "(1, −1) + (2, 1) = (3, 0)."),
    number("Qual é o comprimento do vetor (6, 8)?", "10", "Some 6² + 8² = 100.",
        "A raiz principal de 100 é 10.", "O vetor (5, 12) tem comprimento 13."))

internal fun Curriculum.limits() = lesson(
    "limits", "Limites", "Descreva o valor de aproximação de uma função.",
    "Um limite descreve a saída da função quando a entrada chega perto de um ponto. O valor da função no próprio ponto pode ser diferente ou nem existir.",
    "Ao nos aproximarmos de uma parede por passos cada vez menores, a distância pode tender a zero mesmo antes de encostar.",
    """\lim_{x\to2}(x+1)=3""",
    "x é a entrada. x → 2 indica aproximação a 2. A função x + 1 se aproxima de 3.",
    "Para um limite bilateral, as aproximações pela esquerda e direita precisam convergir ao mesmo número. Em funções contínuas, coincidem com o valor no ponto.",
    Diagram.Line, "Os pontos de uma reta próximos da entrada alvo se aproximam de uma única altura.",
    "Examine as vizinhanças de ambos os lados e compare os valores de aproximação.",
    "Derivadas, integrais e as equações de evolução usam limites.",
    listOf(calculus), listOf("functions"),
    choose("Se as aproximações pela esquerda e direita dão 2 e 4, o que vale para o limite bilateral?", 2, listOf(
        wrong("Vale 3", "O limite não é a média entre comportamentos diferentes.", "Limite e média"),
        wrong("Vale 4", "Este é somente o limite pela direita.", "Limite unilateral"),
        correct("Não existe", "Os limites laterais precisariam coincidir.")
    ), "Compare as duas aproximações.", "Como 2 ≠ 4, elas não definem um valor bilateral.", "Se ambos os lados tendem a 7, o limite é 7."),
    number("Qual é o limite de 2x − 1 quando x se aproxima de 3?", "5", "A função linear é contínua em 3.",
        "Substitua 3 e calcule 2 × 3 − 1 = 5.", "O limite de x + 4 quando x tende a 1 é 5."))

internal fun Curriculum.derivatives() = lesson(
    "derivatives", "Derivadas", "Interprete a taxa instantânea de variação.",
    "Uma derivada indica a taxa de variação em um ponto. No gráfico, é a inclinação da reta tangente quando a função é diferenciável.",
    "A velocidade média usa um intervalo. Encurtar esse intervalo até o instante desejado leva à velocidade instantânea.",
    """f(x)=x^{2}\quad\Rightarrow\quad f'(x)=2x""",
    "f(x) é a função, f′(x) é sua derivada e x é o ponto onde avaliamos a taxa.",
    "O aumento de x² ao somar h a x é 2xh + h². Para h diferente de zero, divida por h. O limite de 2x + h quando h tende a zero é 2x.",
    Diagram.Parabola, "A reta tangente aproxima a curva em torno de um ponto. Sua inclinação mede a derivada.",
    "Uma taxa média vira derivada pelo limite do intervalo de observação.",
    "A evolução de estados quânticos depende de derivadas no tempo e no espaço.",
    listOf(calculus), listOf("limits", "powers"),
    choose("Qual é a derivada de x² em relação a x?", 1, listOf(
        wrong("x", "O expoente cai de 2 para 1 e também vira o coeficiente 2.", "Regra da potência", ErrorKind.Formula),
        correct("2x", "A derivada de um quadrado é duas vezes a variável."),
        wrong("2", "2 é a segunda derivada ou o valor da primeira derivada quando x = 1.", "Função derivada e valor específico")
    ), "Pela regra da potência, passe 2 para o coeficiente.", "Diminua o expoente para 1.", "A derivada de x³ é 3x²."),
    number("Se f(x) = x², qual é a inclinação da tangente em x = 4?", "8", "A derivada é f′(x) = 2x.",
        "Avalie 2 × 4 = 8.", "Em x = 2, a inclinação seria 4."))

internal fun Curriculum.integrals() = lesson(
    "integrals", "Integrais", "Calcule uma quantidade acumulada em um intervalo.",
    "Uma integral definida reúne muitas contribuições pequenas. Para uma função não negativa, mede a área sob sua curva entre os limites.",
    "Se uma torneira fornece 3 litros por minuto durante 4 minutos, o volume acumulado é 12 litros.",
    """\int_{0}^{4}3\,dt=12""",
    "t é o tempo em minutos, dt indica um pequeno incremento de tempo, 3 é a taxa em litros por minuto e 0 a 4 determina o intervalo.",
    "No retângulo, a altura 3 é constante. Com uma taxa variável, subdivida o intervalo, some os retângulos e tome o limite quando suas larguras tendem a zero.",
    Diagram.Area, "A região abaixo de uma taxa constante tem área igual à altura vezes a largura.",
    "Multiplique cada taxa quase constante pelo incremento da variável e acumule.",
    "Probabilidade em uma região contínua é a integral da densidade de probabilidade sobre a região.",
    listOf(calculus), listOf("derivatives"),
    choose("Qual é a unidade da integral de uma taxa em litros/minuto ao longo do tempo em minutos?", 0, listOf(
        correct("Litros", "A largura do intervalo multiplica a taxa e cancela minutos."),
        wrong("Litros/minuto", "Essa é a unidade da altura do gráfico. A integral também inclui a largura em minutos.", "Taxa e quantidade acumulada", ErrorKind.Unit),
        wrong("Minutos", "A largura sozinha mede tempo. Falta multiplicar pela taxa.", "Área e intervalo", ErrorKind.Unit)
    ), "Identifique taxa e tempo.", "Multiplique L/min por min, obtendo L.", "Velocidade em m/s integrada em segundos fornece deslocamento em metros."),
    number("Qual é a integral de f(x) = 5 no intervalo de 1 a 3?", "10", "O intervalo tem largura 3 − 1 = 2.",
        "Multiplique 5 × 2 = 10.", "A integral de 4 entre 0 e 3 vale 12."))

internal fun Curriculum.linearAlgebra() = lesson(
    "linear-algebra", "Álgebra linear", "Expresse um vetor por combinação dos vetores de uma base.",
    "Uma combinação linear é uma soma de vetores multiplicados por números. Uma base permite escrever todo vetor do espaço de forma única.",
    "Em um mapa quadriculado, três passos a leste e dois ao norte identificam um ponto com coordenadas (3, 2).",
    """v=3e_{1}+2e_{2}""",
    "v é o vetor. e₁ e e₂ são os vetores unitários dos dois eixos. 3 e 2 são os coeficientes.",
    "Uma base do plano precisa de dois vetores independentes, isto é, nenhum pode ser múltiplo do outro. Bases diferentes podem representar o mesmo vetor com coeficientes diferentes.",
    Diagram.Vector, "A projeção de um vetor do plano nos dois eixos fornece seus coeficientes na base desses eixos.",
    "Uma base precisa gerar o espaço e ter vetores linearmente independentes.",
    "Em mecânica quântica, trocar a base de um estado ajuda a calcular probabilidades para uma medida diferente.",
    listOf(linear), listOf("vectors"),
    choose("Qual par forma uma base do plano real?", 1, listOf(
        wrong("(1, 0) e (2, 0)", "Ambos são paralelos. Suas combinações alcançam apenas o eixo horizontal.", "Independência linear"),
        correct("(1, 0) e (0, 1)", "As duas direções independentes geram todo o plano."),
        wrong("(0, 0) e (1, 1)", "O vetor zero nada acrescenta. O par alcança somente uma reta.", "Span e vetor zero")
    ), "Procure dois vetores não paralelos e não nulos.", "Os dois vetores unitários geram o plano inteiro.", "(1, 0) e (1, 1) também são uma base."),
    number("Que coeficiente multiplica e₂ em v = 4e₁ − 3e₂?", "-3", "Encontre o termo com e₂.",
        "O seu coeficiente inclui o sinal −: vale −3.", "Em v = −e₁ + 2e₂, o coeficiente de e₂ é 2."))

internal fun Curriculum.matrices() = lesson(
    "matrices", "Matrizes", "Multiplique uma matriz por um vetor usando linhas e colunas.",
    "Uma matriz é uma tabela ordenada. Ao aplicá-la a um vetor coluna, cada linha da matriz determina uma componente da saída.",
    "Uma lanchonete pode calcular o custo de um pedido multiplicando a linha de preços pelas quantidades de cada item.",
    """y_{i}=a_{i1}x_{1}+a_{i2}x_{2}""",
    "x₁ e x₂ são as entradas, aᵢ₁ e aᵢ₂ são valores da linha i e yᵢ é a componente de saída dessa linha.",
    "O número de colunas de A precisa coincidir com o tamanho da entrada x. Uma matriz com m linhas e n colunas transforma n entradas em m saídas.",
    Diagram.Matrix, "Cada fileira do arranjo combina coeficientes com as entradas correspondentes.",
    "Multiplique entrada e coeficiente nas posições correspondentes e some por linha.",
    "Portas de qubits e operadores de observáveis podem ser escritos como matrizes numa base escolhida.",
    listOf(linear), listOf("linear-algebra"),
    choose("Uma matriz de 3 linhas e 2 colunas atua sobre um vetor coluna. Qual é o tamanho da saída?", 2, listOf(
        wrong("2", "2 é o tamanho da entrada. Cada linha tem dois coeficientes.", "Linhas e colunas"),
        wrong("6", "6 conta os elementos da tabela. A saída tem uma componente por linha.", "Tamanho e número de entradas"),
        correct("3", "As três linhas produzem três componentes.")
    ), "Conte as colunas para saber o tamanho da entrada.", "Conte as linhas para saber o tamanho da saída.", "Uma matriz de duas linhas por quatro colunas produz duas saídas."),
    number("A primeira linha de A é (2, 3). Qual é a primeira componente de Ax se x = (4, 1)?", "11",
        "Multiplique 2 × 4 e 3 × 1 nos lugares correspondentes.", "Some 8 + 3 = 11.",
        "A linha (1, −1) aplicada a (5, 2) produz 3."))

internal fun Curriculum.differentialEquations() = lesson(
    "differential-equations", "Equações diferenciais", "Verifique se uma função satisfaz uma lei de evolução.",
    "Uma equação diferencial liga uma função desconhecida às suas taxas de variação. Dada uma condição inicial, buscamos uma função que respeite ambas.",
    "Um material que perde uma proporção fixa da quantidade presente por unidade de tempo segue um decaimento exponencial ideal.",
    """y'(t)=-ky(t)\quad\Rightarrow\quad y(t)=y_{0}e^{-kt}""",
    "y é a quantidade, t é o tempo, k é a taxa positiva, y₀ é o valor inicial e e é a base dos logaritmos naturais.",
    "Derivando a solução, aparece o fator −k multiplicando a própria função. Em t = 0, o exponencial é 1 e recuperamos y₀.",
    Diagram.Decay, "Uma curva de decaimento parte de um valor inicial positivo e se aproxima de zero.",
    "Para verificar uma solução, confira a equação diferencial e o valor inicial.",
    "A equação de Schrödinger relaciona a evolução temporal do estado à ação do Hamiltoniano.",
    listOf(differential), listOf("derivatives", "logarithms"),
    choose("Qual função resolve y′ = −2y com y(0) = 3?", 1, listOf(
        wrong("3e²ᵗ", "Essa função cresce e atende a y′ = +2y.", "Sinal na taxa", ErrorKind.Formula),
        correct("3e⁻²ᵗ", "Derivar produz −2 vezes a função e em t = 0 temos 3."),
        wrong("e⁻²ᵗ", "A equação diferencial é satisfeita, mas o valor inicial é 1.", "Condição inicial")
    ), "Use e⁻²ᵗ para obter fator de derivação −2.", "Multiplique por 3 para que y(0) seja 3.", "y′ = −y com y(0) = 5 tem solução 5e⁻ᵗ."),
    number("Para y(t) = 6e⁻ᵗ, qual é y(0)?", "6", "O expoente em t = 0 é zero.",
        "Como e⁰ = 1, o resultado é 6.", "Para 2e⁻³ᵗ, o valor inicial é 2."))

internal fun Curriculum.fourierTransform() = lesson(
    "fourier", "Transformadas de Fourier", "Interprete um sinal como contribuições de frequências.",
    "O domínio do tempo mostra como um sinal varia. Uma transformada de Fourier representa o quanto ele contém de cada frequência e com que fase.",
    "Ao tocar dois tons puros ao mesmo tempo, a forma temporal fica composta. Um espectro permite identificar os dois tons.",
    """s(t)=\cos(2\pi f_{1}t)+\cos(2\pi f_{2}t)""",
    "s é o sinal, t é o tempo, f₁ e f₂ são frequências em hertz e 2π converte ciclos em ângulo de fase.",
    "Na convenção complexa bilateral, um cosseno ideal tem componentes espectrais em +f e −f. Se ele dura um tempo finito, aparecem bandas de largura finita.",
    Diagram.Modes, "Duas frequências positivas aparecem como dois picos em um espectro unilateral ideal.",
    "Identifique frequências a partir do espectro e confira se a representação é unilateral ou bilateral.",
    "A transformada de Fourier relaciona a representação de posição de uma função de onda à de momento.",
    listOf(fourier, mit), listOf("integrals", "trigonometry", "complex"),
    choose("Em um espectro unilateral ideal, quantos picos vêm de dois cossenos não nulos com frequências positivas distintas?", 0, listOf(
        correct("Dois", "Cada frequência positiva contribui com uma linha espectral."),
        wrong("Um", "Somar os sinais no tempo não funde duas frequências distintas em uma frequência média.", "Soma de sinais e espectro"),
        wrong("Quatro", "Quatro seria a contagem bilateral, com duas frequências negativas também.", "Espectro unilateral e bilateral")
    ), "Conte somente frequências positivas neste espectro.", "Cada cosseno acrescenta uma. O total é dois.",
        "Dois tons de 300 Hz e 500 Hz aparecem em 300 e 500 Hz no espectro unilateral."),
    number("Uma onda de período 0,25 s tem frequência de quantos hertz? Inclua a unidade.", "4",
        "A frequência é o inverso do período.", "Divida 1 por 0,25 s, obtendo 4 Hz.",
        "Período 0,1 s corresponde a frequência 10 Hz.", "Hz"))
