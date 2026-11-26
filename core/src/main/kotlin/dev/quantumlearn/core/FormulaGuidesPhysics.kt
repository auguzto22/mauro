package dev.quantumlearn.core

internal fun physicsGuides(): List<FormulaGuide> = listOf(
    guide("motion", """v_{m}=\frac{\Delta x}{\Delta t}""", "vₘ = Δx / Δt",
        "Velocidade média é o deslocamento dividido pelo tempo gasto.",
        listOf(sym("vₘ", "a velocidade média", "m/s"), sym("Δx", "o deslocamento, posição final menos inicial", "m"),
            sym("Δt", "o intervalo de tempo", "s")),
        listOf(
            step("""\Delta x=x_{f}-x_{i}""", "Δ significa variação: final menos inicial."),
            step("""v_{m}=\frac{100}{10}""", "100 m percorridos em 10 s."),
            step("""v_{m}=10""", "São 10 metros a cada segundo."),
            step("""36\,km/h\div3{,}6=10\,m/s""", "Para passar de km/h para m/s, divida por 3,6.")
        ),
        listOf(
            real(Scene.Sport, "Recorde dos 100 m", "Usain Bolt correu 100 m em 9,58 s em 2009. A média dele foi mais de 10 m/s, cerca de 37,6 km/h.",
                "100 ÷ 9,58 ≈ 10,4 m/s",
                plug("Um atleta corre 400 m em 50 s. Qual a velocidade média?", "8",
                    "Use vₘ = Δx/Δt = 400/50.", "400 ÷ 50 = 8 m/s.", unit = "m/s",
                    trap = "20000", trapWhy = "Velocidade é distância dividida por tempo, não multiplicada.")),
            real(Scene.Transport, "Viagem de carro", "Uma viagem de 240 km em 3 h tem média de 80 km/h, mesmo com paradas e trechos mais rápidos.",
                "240 ÷ 3 = 80 km/h",
                plug("Uma viagem de 360 km leva 4 h. Qual a velocidade média em km/h?", "90",
                    "Use vₘ = 360/4.", "360 ÷ 4 = 90 km/h.", unit = "km/h")),
            real(Scene.Phone, "App de corrida", "Aplicativos mostram o pace, que é o inverso: minutos por quilômetro. 5 km em 25 min dá pace de 5 min/km.",
                "pace = Δt/Δx")
        ),
        iso("Isolando Δx em vₘ = Δx/Δt:", "Δx = vₘ · Δt",
            "Multiplique os dois lados por Δt.",
            "Δx = vₘ / Δt" to "Dividir por Δt de novo não desfaz a divisão.",
            "Δx = Δt / vₘ" to "Essa expressão tem unidade de s²/m, não de metros.")
    ) { a, b ->
        plug("Um corpo se desloca ${a * b} m em $a s. Qual a velocidade média?", "$b",
            "Use vₘ = Δx/Δt = ${a * b}/$a.", "${a * b} ÷ $a = $b m/s.", unit = "m/s",
            trap = "${a * b * a}", trapWhy = "Velocidade divide o deslocamento pelo tempo.")
    },

    guide("forces", """F_{res}=ma""", "F_res = m · a",
        "A força resultante é a massa vezes a aceleração. Mais força, mais aceleração. Mais massa, menos aceleração.",
        listOf(sym("F_res", "a força resultante, soma de todas as forças", "N"), sym("m", "a massa", "kg"),
            sym("a", "a aceleração", "m/s²")),
        listOf(
            step("""F_{res}=10-4=6""", "Some as forças com sinal: empurrar 10 N contra 4 N de atrito dá 6 N."),
            step("""a=\frac{F_{res}}{m}""", "Isole a aceleração."),
            step("""a=\frac{6}{2}=3""", "Uma caixa de 2 kg acelera 3 m/s²."),
            step("""1\,N=1\,kg\cdot m/s^{2}""", "Um newton acelera 1 kg em 1 m/s².")
        ),
        listOf(
            real(Scene.Transport, "Carros e motos", "Para um carro de 1 000 kg ganhar 3 m/s a cada segundo, o motor precisa de 3 000 N de força resultante.",
                "F = 1000 × 3 = 3000 N",
                plug("Uma moto de 200 kg acelera a 4 m/s². Qual a força resultante?", "800",
                    "Use F = m · a.", "200 × 4 = 800 N.", unit = "N",
                    trap = "50", trapWhy = "50 vem de 200 ÷ 4. A força multiplica massa e aceleração.")),
            real(Scene.Sport, "Peso", "O peso é a força da gravidade: P = m · g. Uma pessoa de 70 kg pesa cerca de 686 N.",
                "P = 70 × 9,8 ≈ 686 N",
                plug("Qual o peso de uma mochila de 5 kg? Use g = 10 m/s².", "50",
                    "Use P = m · g.", "5 × 10 = 50 N.", unit = "N")),
            real(Scene.Space, "Foguete", "Na decolagem, o empuxo supera o peso. A diferença é a força resultante que acelera o foguete para cima.",
                "F_res = empuxo − peso")
        ),
        iso("Isolando a aceleração:", "a = F/m",
            "Divida os dois lados de F = m · a por m.",
            "a = F · m" to "Mais massa daria mais aceleração, o contrário do que se observa.",
            "a = m/F" to "A razão está invertida.")
    ) { a, b ->
        plug("Calcule a força resultante sobre $a kg com aceleração de $b m/s².", "${a * b}",
            "Use F = m · a = $a × $b.", "${a * b} N.", unit = "N",
            trap = "${a + b}", trapWhy = "A segunda lei multiplica massa e aceleração.")
    },

    guide("energy", """K=\frac{1}{2}mv^{2}""", "K = ½ · m · v²",
        "Energia cinética é metade da massa vezes a velocidade ao quadrado.",
        listOf(sym("K", "a energia cinética, energia do movimento", "J"), sym("m", "a massa", "kg"),
            sym("v", "a velocidade", "m/s")),
        listOf(
            step("""v^{2}""", "A velocidade entra ao quadrado. Ela pesa mais que a massa."),
            step("""K=\frac{1}{2}\cdot2\cdot3^{2}""", "2 kg a 3 m/s."),
            step("""K=9""", "Resultado: 9 joules."),
            step("""v\to2v\Rightarrow K\to4K""", "Dobrar a velocidade quadruplica a energia.")
        ),
        listOf(
            real(Scene.Transport, "Batidas de carro", "Um carro a 100 km/h tem 4 vezes a energia do mesmo carro a 50 km/h. Os freios precisam dissipar tudo isso como calor.",
                "K ∝ v²",
                plug("Um carro de 1000 kg anda a 20 m/s. Qual sua energia cinética?", "200000",
                    "Use K = ½ · 1000 · 20².", "½ × 1000 × 400 = 200 000 J.", unit = "J",
                    trap = "10000", trapWhy = "10 000 vem de ½ × 1000 × 20, sem elevar a velocidade ao quadrado.")),
            real(Scene.Sport, "Bola de futebol", "Uma bola de 0,43 kg chutada a 30 m/s leva cerca de 194 J.",
                "½ × 0,43 × 30² ≈ 194 J",
                plug("Uma bola de 0,4 kg é chutada a 25 m/s. Qual a energia cinética?", "125",
                    "Use K = ½ · 0,4 · 25².", "½ × 0,4 × 625 = 125 J.", unit = "J",
                    trap = "5", trapWhy = "5 vem de ½ × 0,4 × 25, sem elevar a velocidade ao quadrado.")),
            real(Scene.Home, "Freio regenerativo", "Carros elétricos recuperam parte dessa energia na frenagem e a devolvem à bateria.",
                "K → energia elétrica")
        ),
        iso("Se a velocidade dobra, a energia cinética:", "Fica 4 vezes maior",
            "v aparece ao quadrado: (2v)² = 4v².",
            "Dobra" to "Isso aconteceria se K dependesse de v, não de v².",
            "Fica igual" to "A velocidade é parte da fórmula. Mudar v muda K.")
    ) { a, b ->
        plug("Calcule a energia cinética de $a kg a $b m/s.", num(a * b * b / 2.0),
            "Use K = ½ · $a · $b².", "½ × $a × ${b * b} = ${br(a * b * b / 2.0)} J.", unit = "J",
            trap = num(a * b / 2.0), trapWhy = "A velocidade precisa ser elevada ao quadrado.")
    },

    guide("momentum", """p=mv""", "p = m · v",
        "Momento é massa vezes velocidade. Num sistema isolado, o momento total antes e depois de uma colisão é o mesmo.",
        listOf(sym("p", "o momento linear", "kg·m/s"), sym("m", "a massa", "kg"), sym("v", "a velocidade, com sinal", "m/s")),
        listOf(
            step("""p=mv""", "Um objeto pesado e rápido é difícil de parar."),
            step("""p_{antes}=p_{depois}""", "Sem forças externas, o momento total se conserva."),
            step("""m_{1}v_{1}=(m_{1}+m_{2})v_{f}""", "Numa batida em que os corpos grudam, eles saem juntos."),
            step("""v_{f}=\frac{m_{1}v_{1}}{m_{1}+m_{2}}""", "Isole a velocidade final.")
        ),
        listOf(
            real(Scene.Transport, "Vagões de trem", "Um vagão em movimento engata num parado. Os dois seguem juntos, mais devagar, com o mesmo momento total.",
                "2000 × 3 = 3000 × v ⇒ v = 2 m/s",
                plug("Um vagão de 2000 kg a 3 m/s engata em outro parado de 1000 kg. Qual a velocidade final?", "2",
                    "Conservação: 2000 × 3 = (2000 + 1000) × v.", "v = 6000 ÷ 3000 = 2 m/s.", unit = "m/s",
                    trap = "3", trapWhy = "A massa total aumentou. Com o mesmo momento, a velocidade precisa cair.")),
            real(Scene.Space, "Foguetes", "O foguete empurra gás para trás. O gás ganha momento para trás e o foguete ganha o mesmo momento para frente.",
                "m_gás v_gás = m_foguete v_foguete"),
            real(Scene.Sport, "Patinadores", "Dois patinadores parados se empurram. Saem em sentidos opostos e o mais leve sai mais rápido.",
                "0 = m₁v₁ + m₂v₂",
                plug("Patinadores de 60 kg e 40 kg se empurram. O de 60 kg sai a 2 m/s. Com que rapidez sai o de 40 kg?", "3",
                    "Momento total zero: 60 × 2 = 40 × v.", "v = 120 ÷ 40 = 3 m/s.", unit = "m/s"))
        ),
        iso("Isolando v em p = mv:", "v = p/m",
            "Divida os dois lados por m.",
            "v = p · m" to "Multiplicar de novo por m não isola v.",
            "v = m/p" to "A razão ficou invertida.")
    ) { a, b ->
        plug("Calcule o momento de $a kg a $b m/s, em kg·m/s.", "${a * b}",
            "Use p = m · v = $a × $b.", "${a * b} kg·m/s.",
            trap = "${a + b}", trapWhy = "Momento multiplica massa e velocidade.")
    },

    guide("waves", """v=\lambda f""", "v = λ · f",
        "A velocidade da onda é o comprimento de cada ciclo vezes quantos ciclos passam por segundo.",
        listOf(sym("v", "a velocidade de propagação", "m/s"), sym("λ", "o comprimento de onda, distância entre cristas", "m"),
            sym("f", "a frequência, ciclos por segundo", "Hz")),
        listOf(
            step("""\lambda=3""", "As cristas estão a 3 m uma da outra."),
            step("""f=2""", "Passam 2 cristas por segundo."),
            step("""v=3\times2=6""", "A onda avança 6 m por segundo."),
            step("""\lambda=\frac{v}{f}""", "No mesmo meio v é fixo. Mais frequência, menor comprimento de onda.")
        ),
        listOf(
            real(Scene.Music, "Notas musicais", "No ar o som anda a cerca de 343 m/s. A nota lá de 440 Hz tem comprimento de onda de uns 78 cm.",
                "λ = 343 ÷ 440 ≈ 0,78 m",
                plug("Um som de 686 Hz viaja a 343 m/s. Qual o comprimento de onda?", "0.5",
                    "Isole λ = v/f = 343/686.", "λ = 0,5 m.", unit = "m",
                    trap = "235298", trapWhy = "Multiplicar dá metros²/s². Para achar λ, divida v por f.")),
            real(Scene.Phone, "Wi-Fi", "O Wi-Fi de 2,4 GHz usa ondas de rádio de cerca de 12,5 cm. Por isso as antenas são pequenas.",
                "λ = 3 × 10⁸ ÷ 2,4 × 10⁹ = 0,125 m"),
            real(Scene.Nature, "Ondas do mar", "Ondas com cristas a 20 m chegando a cada 4 s (f = 0,25 Hz) andam a 5 m/s.",
                "v = 20 × 0,25 = 5 m/s",
                plug("Ondas do mar têm cristas a 12 m e frequência de 0,5 Hz. Qual a velocidade?", "6",
                    "Use v = λ · f.", "12 × 0,5 = 6 m/s.", unit = "m/s"))
        ),
        iso("Isolando λ em v = λf:", "λ = v/f",
            "Divida os dois lados por f.",
            "λ = v · f" to "Multiplicar de novo por f não isola λ.",
            "λ = f/v" to "A razão está invertida e daria unidade de 1/m.")
    ) { a, b ->
        plug("Uma onda tem λ = $a m e f = $b Hz. Calcule a velocidade.", "${a * b}",
            "Use v = λ · f = $a × $b.", "${a * b} m/s.", unit = "m/s",
            trap = "$a/$b", trapWhy = "A velocidade multiplica λ por f.")
    },

    guide("electricity", """V=RI\quad P=VI""", "V = R · I · P = V · I",
        "A tensão empurra, a resistência freia e a corrente é o fluxo resultante. A potência é tensão vezes corrente.",
        listOf(sym("V", "a tensão elétrica", "V"), sym("R", "a resistência", "Ω"), sym("I", "a corrente elétrica", "A"),
            sym("P", "a potência elétrica", "W")),
        listOf(
            step("""V=RI""", "Pense numa mangueira: pressão (V), aperto do bico (R) e vazão de água (I)."),
            step("""I=\frac{V}{R}""", "Mais tensão, mais corrente. Mais resistência, menos corrente."),
            step("""I=\frac{12}{4}=3""", "12 V sobre 4 Ω fazem passar 3 A."),
            step("""P=VI=12\times3=36""", "A potência entregue é 36 W.")
        ),
        listOf(
            real(Scene.Home, "Chuveiro elétrico", "Um chuveiro de 5 500 W em 220 V puxa 25 A. Por isso ele precisa de fio grosso e disjuntor próprio.",
                "I = P/V = 5500 ÷ 220 = 25 A",
                plug("Um chuveiro de 4400 W está ligado em 220 V. Qual a corrente?", "20",
                    "Isole I = P/V.", "4400 ÷ 220 = 20 A.", unit = "A",
                    trap = "968000", trapWhy = "Para a corrente, divida a potência pela tensão.")),
            real(Scene.Phone, "Carregador", "Um carregador USB de 5 V e 2 A entrega 10 W. Carregadores rápidos sobem a tensão para entregar mais potência.",
                "P = 5 × 2 = 10 W",
                plug("Um carregador entrega 9 V e 2 A. Qual a potência, em watts?", "18",
                    "Use P = V · I.", "9 × 2 = 18 W.")),
            real(Scene.Home, "Resistor de LED", "Para ligar um LED em 5 V, um resistor limita a corrente. Com 3 V sobre um resistor de 150 Ω, passam 20 mA.",
                "I = 3 ÷ 150 = 0,02 A")
        ),
        iso("Isolando a corrente em V = RI:", "I = V/R",
            "Divida os dois lados por R.",
            "I = V · R" to "Mais resistência daria mais corrente, o contrário do que acontece.",
            "I = R/V" to "A razão está invertida.")
    ) { a, b ->
        plug("Um resistor de $a Ω conduz $b A. Qual a tensão sobre ele?", "${a * b}",
            "Use V = R · I = $a × $b.", "${a * b} V.", unit = "V",
            trap = "$a/$b", trapWhy = "A tensão multiplica resistência e corrente.")
    },

    guide("magnetism", """F=|q|vB\,\sin\ \theta""", "F = |q| · v · B · sen θ",
        "Uma carga em movimento num campo magnético sente força proporcional à carga, à velocidade, ao campo e ao seno do ângulo.",
        listOf(sym("F", "a força magnética", "N"), sym("q", "a carga elétrica", "C"), sym("v", "a velocidade da carga", "m/s"),
            sym("B", "a intensidade do campo magnético", "T"), sym("θ", "o ângulo entre v e B")),
        listOf(
            step("""v=0\Rightarrow F=0""", "Carga parada não sente força magnética."),
            step("""\sin\ 90°=1""", "Movimento perpendicular ao campo dá a força máxima."),
            step("""\sin\ 0°=0""", "Movimento paralelo ao campo não sofre força."),
            step("""F=2\cdot3\cdot0{,}5\cdot1=3""", "2 C a 3 m/s, perpendicular a 0,5 T: 3 N.")
        ),
        listOf(
            real(Scene.Nature, "Auroras", "Partículas do vento solar são desviadas pelo campo da Terra e guiadas aos polos, onde fazem o céu brilhar.",
                "F ⊥ v: a trajetória curva",
                plug("Um próton (q = 1,6e-19 C) a 4e5 m/s cruza perpendicularmente o campo da Terra, B = 5e-5 T. Qual a força, em N?",
                    "1.6e-19*4e5*5e-5", "Use F = q v B sen 90° = 1,6e-19 × 4e5 × 5e-5 × 1.", "F = 3,2e-18 N.", unit = "N",
                    trap = "0", trapWhy = "Perpendicular significa sen 90° = 1, a força máxima.", tolerance = 1e-6)),
            real(Scene.Health, "Ressonância magnética", "Aparelhos de ressonância usam campos de 1,5 a 3 T, dezenas de milhares de vezes o campo da Terra.",
                "B ≈ 1,5 T a 3 T"),
            real(Scene.Home, "Alto-falantes e motores", "A corrente no fio do alto-falante sente força do ímã e empurra o cone, que empurra o ar.",
                "F = I L B")
        ),
        iso("Com v paralela a B (θ = 0°), quanto vale F?", "0",
            "sen 0° = 0 anula a força.",
            "q v B" to "Esse é o valor máximo, quando θ = 90°.",
            "q v B / 2" to "sen 0° vale 0, não 1/2.")
    ) { a, b ->
        plug("Uma carga de $a C anda a $b m/s perpendicular a um campo de 2 T. Qual a força?", "${2 * a * b}",
            "Use F = q v B sen 90° = $a × $b × 2 × 1.", "${2 * a * b} N.", unit = "N",
            trap = "${a * b}", trapWhy = "O campo B = 2 T também multiplica.")
    },

    guide("electromagnetic-fields", """c=\lambda f""", "c = λ · f",
        "No vácuo, toda onda eletromagnética anda a c ≈ 3 × 10⁸ m/s. Frequência e comprimento de onda são inversos.",
        listOf(sym("c", "a velocidade da luz no vácuo, cerca de 3 × 10⁸", "m/s"), sym("λ", "o comprimento de onda", "m"),
            sym("f", "a frequência", "Hz")),
        listOf(
            step("""c\approx3\times10^{8}""", "Rádio, micro-ondas, luz e raios X: todos andam a c."),
            step("""\lambda=\frac{c}{f}""", "Isole o comprimento de onda."),
            step("""\lambda=\frac{3\times10^{8}}{100\times10^{6}}=3""", "Uma rádio FM de 100 MHz tem ondas de 3 m."),
            step("""f\uparrow\Rightarrow\lambda\downarrow""", "Mais frequência, menor comprimento: do rádio aos raios gama.")
        ),
        listOf(
            real(Scene.Home, "Micro-ondas", "O forno usa 2,45 GHz, com λ ≈ 12 cm. A grade da porta tem furos bem menores que isso e bloqueia as ondas.",
                "λ = 3 × 10⁸ ÷ 2,45 × 10⁹ ≈ 0,12 m"),
            real(Scene.Phone, "Rádio FM", "Antenas costumam ter tamanho comparável ao comprimento de onda.",
                "100 MHz ⇒ λ = 3 m",
                plug("Uma rádio FM transmite em 100 MHz (1e8 Hz). Qual o comprimento de onda? Use c = 3e8 m/s.", "3",
                    "Use λ = c/f = 3e8/1e8.", "λ = 3 m.", unit = "m",
                    trap = "3e16", trapWhy = "Para λ, divida c pela frequência.")),
            real(Scene.Light, "Cores", "A luz verde tem λ ≈ 530 nm. Sua frequência passa de 500 trilhões de oscilações por segundo.",
                "f = 3 × 10⁸ ÷ 530 × 10⁻⁹ ≈ 5,7 × 10¹⁴ Hz")
        ),
        iso("Se a frequência aumenta, o comprimento de onda no vácuo:", "Diminui",
            "c é fixo, então λ = c/f cai quando f sobe.",
            "Aumenta" to "λ e f são inversamente proporcionais.",
            "Não muda" to "Só c fica fixo no vácuo. λ acompanha a frequência.")
    ) { a, _ ->
        plug("Uma onda no vácuo tem f = $a × 10⁸ Hz. Qual λ? Use c = 3e8 m/s.", "3/$a",
            "Use λ = c/f = 3e8/(${a}e8).", "λ = 3/$a ≈ ${br(3.0 / a)} m.", unit = "m",
            trap = "${3 * a}", trapWhy = "Divida c pela frequência, não multiplique.", tolerance = 1e-4)
    }
)
