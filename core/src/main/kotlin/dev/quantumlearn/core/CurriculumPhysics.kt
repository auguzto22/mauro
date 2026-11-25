package dev.quantumlearn.core

internal fun Curriculum.motion() = lesson(
    "motion", "Movimento", "Calcule a velocidade média em uma trajetória unidimensional.",
    "Movimento descreve como a posição muda ao longo do tempo. Em uma dimensão, a velocidade média usa o deslocamento com sinal dividido pelo tempo decorrido.",
    "Você anda 5 m para a direita e volta 2 m para a esquerda em três segundos. A distância percorrida é 7 m, mas o deslocamento é +3 m.",
    """v_{m}=\frac{\Delta x}{\Delta t}""",
    "vₘ é a velocidade média. Δx é a posição final menos a inicial. Δt é o tempo final menos o inicial.",
    "Em um gráfico de posição por tempo, a velocidade média é a inclinação da reta que liga os pontos nos extremos. Para velocidade constante, essa reta coincide com o gráfico.",
    Diagram.Line, "A inclinação positiva significa que a posição aumenta no intervalo.",
    "Use a variação da posição e preserve seu sinal ao calcular velocidade média.",
    "Essa distinção permite interpretar trajetórias e estimar grandezas em cinemática.",
    listOf(mechanics), listOf("functions", "vectors"),
    choose("Uma pessoa caminha e retorna ao ponto inicial após 10 s. Qual é sua velocidade média no percurso completo?", 2, listOf(
        wrong("É positiva", "Pode haver distância percorrida positiva, mas a diferença entre as posições inicial e final é zero.", "Distância e deslocamento"),
        wrong("É negativa", "A volta tem sentido oposto à ida, e o deslocamento total cancela.", "Deslocamentos com sinal"),
        correct("É zero", "Com Δx = 0 e tempo positivo, a razão é zero.")
    ), "Subtraia a mesma posição inicial da final.", "Divida zero por 10 s.", "Caminhar −4 m em 2 s dá velocidade média −2 m/s."),
    number("O deslocamento foi +12 m em 4 s. Qual é a velocidade média? Inclua a unidade.", "3",
        "Use vₘ = Δx/Δt.", "Divida 12 m por 4 s para obter 3 m/s.", "8 m em 2 s correspondem a 4 m/s.", "m/s"))

internal fun Curriculum.forces() = lesson(
    "forces", "Forças", "Relacione força resultante e aceleração.",
    "No modelo de Newton, a força resultante muda a velocidade de um corpo com massa constante. Some as forças como vetores antes de calcular a aceleração.",
    "Dois empurrões opostos de 10 N e 6 N em uma caixa deixam uma força resultante de 4 N no sentido do empurrão maior.",
    """F_{res}=ma""",
    "A equação mostra as componentes em um eixo. F res é a força resultante, m é a massa e a é a aceleração.",
    "A relação vale em um referencial inercial e no regime clássico. Força resultante nula implica velocidade constante, que pode ser diferente de zero.",
    Diagram.Balance, "O equilíbrio ilustra que contribuições opostas de mesma magnitude podem se cancelar.",
    "A força resultante produz aceleração. Velocidade constante é compatível com força resultante nula.",
    "Engenharia usa forças resultantes para prever o movimento de veículos e equipamentos.",
    listOf(mechanics), listOf("motion", "vectors"),
    choose("Uma caixa desliza para a direita com velocidade constante. Qual é a força resultante?", 1, listOf(
        wrong("Aponta à direita", "Força para a direita aceleraria a caixa. A velocidade não está mudando.", "Velocidade e aceleração"),
        correct("É nula", "Sem aceleração, ma é zero."),
        wrong("Aponta à esquerda", "Uma força à esquerda reduziria a velocidade para a direita.", "Direção da força resultante")
    ), "Velocidade constante significa aceleração zero.", "Aplique a segunda lei de Newton.", "Um corpo em repouso sobre uma mesa pode ter peso e força normal que se cancelam."),
    number("Um corpo de massa 2 kg tem aceleração de 3 m/s². Qual é a magnitude da força resultante? Inclua a unidade.",
        "6", "Multiplique massa e aceleração.", "2 × 3 = 6 kg·m/s² = 6 N.", "3 kg a 2 m/s² também exigem 6 N.", "N"))

internal fun Curriculum.energy() = lesson(
    "energy", "Energia", "Calcule a energia cinética de um corpo no regime clássico.",
    "Energia cinética é associada ao movimento. Para velocidades muito menores que a da luz, ela cresce com o quadrado da rapidez.",
    "Se duas bolas idênticas têm velocidades de 2 m/s e 4 m/s, a segunda tem quatro vezes a energia cinética da primeira.",
    """K=\frac{1}{2}mv^{2}""",
    "K é a energia cinética em joules, m é a massa em kg e v é a rapidez em m/s.",
    "A energia cinética depende do referencial usado para medir a velocidade. A conservação da energia total inclui outras formas, como potencial e energia interna.",
    Diagram.Parabola, "Uma curva quadrática fica mais inclinada conforme cresce a entrada.",
    "Ao dobrar a rapidez com massa fixa, a energia cinética quadruplica.",
    "Energia é indispensável para entender níveis discretos e barreiras de potencial.",
    listOf(mechanics), listOf("forces", "powers"),
    choose("Mantendo a massa, o que acontece com K se a rapidez triplica?", 0, listOf(
        correct("Fica nove vezes maior", "O fator de velocidade é elevado ao quadrado."),
        wrong("Fica três vezes maior", "Esse seria um aumento linear. A energia cinética contém v².", "Dependência linear e quadrática"),
        wrong("Fica seis vezes maior", "O quadrado de 3 é 3 × 3, não 3 × 2.", "Potência e multiplicação", ErrorKind.Calculation)
    ), "Substitua v por 3v.", "Como (3v)² = 9v², K multiplica por 9.", "Reduzir v à metade reduz K a 1/4 do original."),
    number("Calcule a energia cinética de uma bola de 2 kg a 3 m/s. Inclua a unidade.", "9",
        "Calcule v² = 9 m²/s².", "Multiplique por 2 kg e divida por 2, obtendo 9 J.", "Para 4 kg a 2 m/s, K vale 8 J.", "J"))

internal fun Curriculum.momentum() = lesson(
    "momentum", "Momento", "Calcule o momento linear e reconheça sua conservação.",
    "Momento linear combina massa e velocidade. Como a velocidade tem direção, o momento também tem. Em um sistema isolado, o momento total se conserva.",
    "Em uma pista sem atrito externo significativo, patinadores que se empurram ganham momentos de sinais opostos. Se começaram parados, a soma continua zero.",
    """p=mv""",
    "A equação mostra a componente em um eixo. p é o momento linear, m é a massa e v é a velocidade com sinal.",
    "As forças internas podem redistribuir momento entre os corpos. Para o total do sistema, apenas um impulso externo líquido muda a soma.",
    Diagram.Vector, "A projeção do vetor em um eixo tem sinal e muda ao inverter a direção.",
    "Momento é vetorial. Some as contribuições com sinais antes e depois da interação.",
    "Momento também aparece na relação entre ondas de matéria e comprimento de onda.",
    listOf(mechanics), listOf("motion", "forces"),
    choose("Dois patinadores num sistema isolado partem do repouso e se empurram. Se um fica com momento +4, qual é o momento do outro no mesmo eixo?", 1, listOf(
        wrong("+4", "Dois momentos positivos somariam +8, mas o total inicial era zero.", "Sinal do momento"),
        correct("−4", "As duas contribuições somam zero."),
        wrong("Zero", "Um único momento +4 não conservaria o total. O empurrão afeta ambos.", "Momento individual e total")
    ), "Calcule o momento inicial total, zero.", "Escolha −4 para cancelar +4.", "Se um ganha +7, o outro ganha −7 no mesmo eixo."),
    number("Um objeto de 3 kg tem velocidade −2 m/s no eixo. Calcule o valor numérico do momento nesse eixo em kg·m/s.",
        "-6", "Multiplique 3 por −2.", "O momento fica −6 kg·m/s; o sinal indica o sentido.", "Uma massa de 5 kg a +2 m/s tem momento +10 kg·m/s."))

internal fun Curriculum.electricity() = lesson(
    "electricity", "Eletricidade", "Preveja a corrente em um resistor ôhmico.",
    "Corrente mede quanta carga passa por unidade de tempo. Uma diferença de potencial impulsiona corrente em um circuito fechado.",
    "Uma resistência de 3 Ω ligada a uma bateria ideal de 6 V conduz 2 A.",
    """V=RI""",
    "V é a diferença de potencial em volts, R é a resistência em ohms e I é a corrente em ampères.",
    "A lei de Ohm nessa forma pressupõe resistência constante no intervalo de funcionamento. Alguns dispositivos mudam de resistência com a temperatura ou com a tensão.",
    Diagram.Circuit, "O caminho fechado esquemático liga uma fonte a um resistor.",
    "Sob resistência constante, a corrente é a tensão dividida pela resistência.",
    "Circuitos e sensores convertem variações de tensão e corrente em sinais mensuráveis.",
    listOf(fields), listOf("equations", "energy"),
    choose("Para um resistor ôhmico com tensão constante, o que acontece se a resistência dobra?", 2, listOf(
        wrong("A corrente dobra", "Resistência fica no denominador de I = V/R.", "Resistência e corrente"),
        wrong("A corrente fica igual", "A mesma tensão sobre resistências distintas produz correntes diferentes.", "Tensão fixa e corrente variável"),
        correct("A corrente cai à metade", "Dividir pela nova resistência 2R produz metade da corrente.")
    ), "Isole I como V/R.", "Substitua R por 2R e obtenha I/2.", "Com 12 V, 3 Ω conduz 4 A e 6 Ω conduz 2 A."),
    number("Qual é o valor numérico da corrente, em ampères, com 12 V e 4 Ω?", "3", "Use I = V/R.",
        "Divida 12 por 4, obtendo 3 A.", "Com 10 V e 5 Ω, I vale 2 A."))

internal fun Curriculum.magnetism() = lesson(
    "magnetism", "Magnetismo", "Relacione a direção do movimento à força magnética.",
    "Um campo magnético pode desviar uma carga em movimento. Para um campo uniforme, a força magnética é perpendicular à velocidade e ao campo.",
    "Uma carga entrando perpendicularmente em um campo uniforme pode descrever um círculo, pois a força muda sua direção de movimento.",
    """F=|q|vB\sin\theta""",
    "F é a magnitude da força, q é a carga, v é a rapidez, B é a magnitude do campo magnético e θ é o ângulo entre velocidade e campo.",
    "A direção da força para carga positiva segue o produto vetorial da velocidade pelo campo. Para carga negativa, inverta o sentido. A rapidez não muda sob a força magnética isolada.",
    Diagram.Vector, "Dois eixos perpendiculares ajudam a pensar no ângulo de 90° que maximiza o seno.",
    "A força magnética é máxima para velocidade perpendicular ao campo, e zero para movimento paralelo.",
    "Campos magnéticos desviam feixes de partículas em espectrômetros.",
    listOf(fields), listOf("vectors", "trigonometry", "electricity"),
    choose("Uma carga se move paralelamente a um campo magnético uniforme. Qual é a força magnética?", 0, listOf(
        correct("Zero", "O seno do ângulo entre as direções paralelas é zero."),
        wrong("Máxima", "O seno vale 1 quando as direções são perpendiculares, não paralelas.", "Ângulo entre campo e velocidade"),
        wrong("Aponta ao longo da velocidade", "A força magnética, quando não nula, é perpendicular à velocidade.", "Direção da força")
    ), "Use θ = 0 para movimento no mesmo sentido do campo.", "sin 0 = 0, então F = 0.",
        "No sentido oposto, θ = 180° e a força também é zero."),
    number("A força teria magnitude 8 N em ângulo de 90°. Quanto vale em 30°, com as mesmas magnitudes? Use sen 30° = 1/2 e inclua unidade.",
        "4", "O seno em 90° vale 1.", "Em 30°, multiplique o máximo 8 N por 1/2.", "Se a força máxima é 6 N, a de 30° vale 3 N.", "N"))

internal fun Curriculum.electromagneticFields() = lesson(
    "electromagnetic-fields", "Campos eletromagnéticos", "Reconheça a natureza transversal de uma onda eletromagnética plana.",
    "A luz é uma onda eletromagnética. Em uma onda plana no vácuo, os campos elétrico e magnético oscilam em direções perpendiculares entre si e à propagação.",
    "Em uma antena, cargas aceleradas podem produzir ondas que se propagam pelo espaço levando energia.",
    """c=\lambda f""",
    "c é a velocidade da luz no vácuo, λ é o comprimento de onda e f é a frequência.",
    "O par de campos pode existir no vácuo e a orientação do campo elétrico define a polarização linear. A fórmula c = λf se aplica a ondas no vácuo.",
    Diagram.Wave, "O perfil senoidal mostra um componente transversal variando ao longo da direção de propagação.",
    "Na onda plana, a oscilação é transversal e sua velocidade no vácuo é c.",
    "Ondas de rádio, micro-ondas e luz visível diferem em frequência e comprimento de onda.",
    listOf(fields, nist), listOf("waves", "electricity", "magnetism"),
    choose("Na onda plana no vácuo, como se orienta o campo elétrico em relação à propagação?", 1, listOf(
        wrong("Paralelo", "Essa é a estrutura de uma onda longitudinal. O campo da onda plana eletromagnética é transversal.", "Ondas transversais e longitudinais"),
        correct("Perpendicular", "O campo elétrico oscila em direção transversal."),
        wrong("Forma sempre 45°", "As direções são ortogonais. 45° pode caracterizar polarização relativa a outro eixo transversal.", "Polarização e propagação")
    ), "Identifique a direção em que o perfil avança.", "O campo elétrico é perpendicular a essa direção.",
        "Uma onda que avança em z pode ter campo elétrico oscilando em x e campo magnético em y."),
    number("Use c = 3e8 m/s. Calcule o comprimento de onda para f = 1e8 Hz e inclua a unidade.", "3",
        "Isole λ = c/f.", "Divida 3 × 10⁸ por 10⁸ para obter 3 m.", "Em 3e8 Hz, λ é 1 m no mesmo arredondamento.", "m"))

internal fun Curriculum.blackbody() = lesson(
    "blackbody", "Radiação de corpo negro", "Preveja como a temperatura altera o pico de emissão por comprimento de onda.",
    "Um corpo negro ideal absorve toda radiação incidente. Em equilíbrio térmico, seu espectro emitido depende apenas da temperatura.",
    "Uma peça aquecida pode brilhar em vermelho e, a temperaturas mais altas, emitir mais luz de comprimentos de onda menores.",
    """\lambda_{p}T=b""",
    "λ p é o comprimento de onda do pico por unidade de comprimento de onda. T é a temperatura absoluta em kelvins e b é a constante de deslocamento de Wien.",
    "Para esta representação do espectro, b vale aproximadamente 2,898 × 10⁻³ m·K. O pico em função da frequência requer outra relação; converter diretamente o λ do pico não dá o pico em frequência.",
    Diagram.Bell, "Dois espectros esquemáticos marcam o pico à esquerda para o corpo mais quente.",
    "Aumentar a temperatura desloca o pico por comprimento de onda para comprimentos menores.",
    "A forma do espectro térmico permite estimar temperaturas efetivas de estrelas.",
    listOf(Reference("OpenStax · Radiação de corpo negro", "https://openstax.org/books/university-physics-volume-3/pages/6-1-blackbody-radiation"), nist),
    listOf("scientific", "electromagnetic-fields"),
    choose("O que acontece com λ p se a temperatura absoluta de um corpo negro dobra?", 2, listOf(
        wrong("Dobra", "λ p e T entram como produto constante, não como razão constante.", "Relação inversa"),
        wrong("Continua igual", "Um mesmo pico com o dobro da temperatura não preserva o produto λ p T.", "Temperatura e espectro"),
        correct("Cai à metade", "λ p = b/T. Dobrar T divide λ p por dois.")
    ), "Isole λ p = b/T.", "Substitua T por 2T.", "Triplicar a temperatura divide λ p por três."),
    number("Se b = 2,9e-3 m·K e T = 2900 K, qual é o comprimento de onda do pico? Inclua a unidade.", "1e-6",
        "Divida b pela temperatura absoluta.", "2,9e-3 / 2900 = 1e-6 m.", "A 5800 K, o mesmo arredondamento dá 500 nm.", "m"))

internal fun Curriculum.photoelectricEffect() = lesson(
    "photoelectric", "Efeito fotoelétrico", "Distinga a frequência limiar da intensidade da luz incidente.",
    "No efeito fotoelétrico de um fóton, um elétron pode sair de um material quando a energia do fóton alcança a energia mínima de extração.",
    "Se um metal exige 2 eV para extrair um elétron, um fóton de 5 eV pode deixar até 3 eV de energia cinética.",
    """K_{max}=hf-\phi""",
    "K máx é a energia cinética máxima dos elétrons emitidos, h é a constante de Planck, f é a frequência e φ é a função trabalho do material.",
    "A relação vale acima do limiar f = φ/h no modelo de absorção de um fóton. Abaixo desse limiar, o modelo não prevê emissão por esse processo.",
    Diagram.Threshold, "A energia cinética máxima cresce linearmente a partir do ponto limiar onde vale zero.",
    "A frequência controla a energia por fóton. A intensidade pode alterar o número de eventos acima do limiar.",
    "O princípio explica como alguns fotodetectores convertem luz em emissão de elétrons.",
    listOf(photon, nist), listOf("photons", "energy"),
    choose("A energia do fóton é 1 eV e a função trabalho é 2 eV. O que prevê o modelo de um fóton?", 1, listOf(
        wrong("Elétrons de energia −1 eV", "Energia cinética não é negativa. O cálculo negativo indica ausência de emissão nesse modelo.", "Limiar e resultado físico"),
        correct("Sem emissão", "O fóton individual não tem a energia mínima de extração."),
        wrong("Emissão se aumentarmos a intensidade", "Neste regime, mais fótons de 1 eV não aumentam a energia de cada um.", "Intensidade e frequência")
    ), "Compare 1 eV do fóton com 2 eV de extração.", "Como 1 < 2, o processo de um fóton fica abaixo do limiar.",
        "Um fóton de 4 eV com função trabalho 2 eV permite energia cinética máxima de 2 eV."),
    number("Fótons de 6 eV iluminam um metal com função trabalho de 2,5 eV. Qual é K máximo? Inclua a unidade.",
        "3.5", "Subtraia a função trabalho da energia do fóton.", "6 − 2,5 = 3,5 eV.", "Com 5 eV e 2 eV de extração, K máximo é 3 eV.", "eV"))

internal fun Curriculum.duality() = lesson(
    "duality", "Dualidade onda-partícula", "Relacione o padrão de matéria à relação de de Broglie.",
    "Elétrons produzem detecções localizadas e, em condições de coerência, um padrão de difração acumulado. A descrição quântica contempla ambas as observações.",
    "Cristais podem difratar elétrons. Ajustar o momento do feixe altera os ângulos em que surgem máximos.",
    """\lambda=\frac{h}{p}""",
    "λ é o comprimento de onda de de Broglie, h é a constante de Planck e p é a magnitude do momento.",
    "A relação vale para uma componente de momento definido. Uma partícula localizada costuma ser descrita por uma superposição de componentes com momentos distintos.",
    Diagram.Interference, "Um conjunto de regiões claras esquematiza os máximos de probabilidade de detecção em um padrão de interferência.",
    "Quanto maior o momento, menor o comprimento de onda associado à componente.",
    "O pequeno comprimento de onda de elétrons permite criar imagens de estruturas muito pequenas.",
    listOf(Reference("OpenStax · Ondas de matéria", "https://openstax.org/books/university-physics-volume-3/pages/6-5-de-broglies-matter-waves"), feynman),
    listOf("photons", "momentum"),
    choose("Se o momento de um elétron dobra, como muda seu comprimento de onda de de Broglie?", 0, listOf(
        correct("Cai à metade", "p aparece no denominador e h é constante."),
        wrong("Dobra", "Essa resposta corresponderia a λ proporcional a p. Aqui a relação é inversa.", "Proporcionalidade inversa"),
        wrong("Fica igual", "A constância de h não mantém λ fixo quando p muda.", "Constante e variável")
    ), "Use λ = h/p.", "Substitua p por 2p para obter λ/2.",
        "Se p é multiplicado por 4, λ fica quatro vezes menor."),
    number("Um feixe tem comprimento de onda de 0,6 nm. O momento passa a ser três vezes maior. Qual é o novo comprimento de onda? Inclua a unidade.",
        "0.2", "O fator de mudança do comprimento de onda é 1/3.", "Divida 0,6 nm por 3.", "Se 9 nm e o momento triplica, o novo valor é 3 nm.", "nm"))

internal fun Curriculum.uncertainty() = lesson(
    "uncertainty", "Princípio da incerteza", "Interprete o limite para dispersões de posição e momento.",
    "Um estado quântico pode produzir uma faixa de posições e uma faixa de momentos em medidas sobre preparações repetidas. As duas dispersões obedecem a um limite.",
    "Um pulso curto contém uma faixa maior de frequências. A ligação entre largura espacial e largura de momento segue uma ideia semelhante de Fourier.",
    """\Delta x\,\Delta p\ge\frac{\hbar}{2}""",
    "Δx e Δp são desvios padrão de posição e momento no mesmo eixo. ℏ é a constante de Planck reduzida e ≥ indica um limite inferior.",
    "Se Δx diminuir, o menor Δp permitido aumenta. Nem todo estado satura o limite. A relação descreve o estado e não depende apenas de um instrumento imperfeito.",
    Diagram.Bell, "Curvas com larguras diferentes ajudam a visualizar a dispersão de uma distribuição.",
    "A desigualdade limita o produto das dispersões e permite que o produto seja maior que ℏ/2.",
    "Pacotes de onda em microscopia e estados ligados exigem considerar dispersões de posição e momento.",
    listOf(mit, nist), listOf("fourier", "duality", "probability"),
    choose("Um estado tem Δx menor. O que muda no limite inferior permitido para Δp?", 2, listOf(
        wrong("Cai também", "A relação Δp ≥ ℏ/(2Δx) aumenta o limite quando o denominador diminui.", "Proporcionalidade inversa"),
        wrong("Fica sempre zero", "Com Δx positivo e finito, o lado direito é positivo.", "Limite inferior"),
        correct("Aumenta", "Uma posição menos espalhada exige um limite de momento maior.")
    ), "Isole Δp na desigualdade para Δx positivo.", "Um denominador menor dá um limite maior.",
        "Se um estado no limite tem Δx reduzido à metade, o Δp mínimo dobra."),
    number("Se Δx é reduzido a um quarto, por qual fator o limite inferior de Δp aumenta?", "4",
        "O limite é proporcional a 1/Δx.", "O inverso de 1/4 é 4.",
        "Reduzir Δx a um quinto aumenta o limite inferior por um fator 5."))
