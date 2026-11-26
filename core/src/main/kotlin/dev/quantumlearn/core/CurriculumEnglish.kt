package dev.quantumlearn.core

/*
 * Trilha de inglês: só a gramática que costuma travar quem já conhece o vocabulário.
 * As perguntas de lacuna usam a convenção "instrução\nfrase com ___".
 */

private val englishSources get() = listOf(Curriculum.english, Curriculum.cambridge)

internal fun Curriculum.englishPronouns() = lesson(
    "en-pronouns", "Pronomes pessoais", "Saiba quem faz a ação: I, you, he, she, it, we, they.",
    "Em inglês, toda frase precisa de um sujeito escrito. Em português dá para dizer \"Sou estudante\". Em inglês é obrigatório dizer quem: \"I am a student\". O pronome ocupa o lugar do nome da pessoa ou da coisa.",
    "Ana é professora. Ela mora em Recife. Em inglês: Ana is a teacher. She lives in Recife. \"She\" troca \"Ana\" para não repetir o nome.",
    "I,\\ you,\\ he,\\ she,\\ it,\\ we,\\ they",
    "I = eu (sempre maiúsculo). You = você ou vocês. He = ele. She = ela. It = coisa ou animal. We = nós. They = eles ou elas.",
    "\"It\" é usado para objetos, animais e também para tempo e clima: It is hot (Está quente), It is 3 o'clock (São 3 horas). \"You\" serve para singular e plural, o contexto mostra qual é. \"They\" vale para pessoas e coisas.",
    Diagram.Sentence, "O sujeito é o primeiro bloco da frase. Sem ele, a frase em inglês fica incompleta.",
    "Toda frase em inglês tem sujeito. Use o pronome certo para não repetir nomes.",
    "Você usa pronomes em qualquer conversa: para falar de você (I), de um amigo (he/she) ou de um grupo (they).",
    englishSources, emptyList(),
    choose("Troque o nome pelo pronome:\nMaria and Paulo are friends. ___ are friends.", 2, listOf(
        wrong("He", "He é só um homem. Maria e Paulo são duas pessoas.", "Singular e plural"),
        wrong("We", "We inclui quem fala (nós). Você está falando de outras pessoas.", "Quem está incluído no grupo"),
        correct("They", "Duas ou mais pessoas, sem incluir você: they."),
        wrong("She", "She é só uma mulher. Aqui há duas pessoas.", "Singular e plural")
    ), "Conte as pessoas: Maria e Paulo são duas.", "Você não está no grupo, então é they (eles).",
        "My mother and I → we (você está incluído)."),
    choose("Qual pronome usar para um cachorro?\nThe dog is hungry. ___ wants food.", 1, listOf(
        wrong("He", "He é para homens. Para animais, o padrão é it, a não ser que seja um bicho de estimação tratado como pessoa.", "Pronome para animais e coisas"),
        correct("It", "Animais e coisas usam it."),
        wrong("They", "They é plural. Há um cachorro só.", "Singular e plural")
    ), "Identifique o sujeito: um cachorro.", "Animal ou coisa no singular: it.", "The phone is new. It is black."),
    choose("Como dizer \"Sou estudante\" em inglês?", 0, listOf(
        correct("I am a student.", "O sujeito I precisa aparecer, mesmo que em português ele fique escondido."),
        wrong("Am a student.", "Em inglês o sujeito não pode ser omitido. Falta o I.", "Sujeito obrigatório", ErrorKind.Formula),
        wrong("Is a student.", "Falta o sujeito e o verbo está na forma de he/she/it.", "Sujeito obrigatório", ErrorKind.Formula)
    ), "Em português o \"eu\" está escondido em \"sou\".", "Em inglês, escreva o sujeito: I am a student.", "Estamos cansados → We are tired.")
)

internal fun Curriculum.englishToBe() = lesson(
    "en-to-be", "Verbo to be: am, is, are", "Use ser e estar em inglês sem errar am, is e are.",
    "O verbo to be significa ser e estar ao mesmo tempo. Ele muda conforme o sujeito: I am, he/she/it is, you/we/they are. É o verbo mais usado do inglês.",
    "I am Brazilian (Eu sou brasileiro). She is at home (Ela está em casa). They are happy (Eles estão felizes).",
    "Sujeito+am/is/are+complemento",
    "Sujeito = quem. am = só com I. is = he, she, it ou um nome no singular. are = you, we, they ou plural. Complemento = o que a pessoa é ou como/onde está.",
    "Na fala, quase sempre se usa a forma curta: I'm, you're, he's, she's, it's, we're, they're. Um nome no singular funciona como he/she/it: My father is tall. Um nome no plural funciona como they: My parents are tall.",
    Diagram.Sentence, "Três blocos: quem (sujeito), o verbo to be no meio e o complemento no fim.",
    "I → am. He, she, it, singular → is. You, we, they, plural → are.",
    "Para se apresentar, dizer onde está, como se sente, a profissão ou a idade: I am 20 years old.",
    englishSources, listOf("en-pronouns"),
    choose("Complete:\nShe ___ a doctor.", 1, listOf(
        wrong("am", "Am é exclusivo do I.", "Am só com I", ErrorKind.Formula),
        correct("is", "She é terceira pessoa do singular: is."),
        wrong("are", "Are é para you, we e they.", "Is para he, she, it", ErrorKind.Formula)
    ), "Veja o sujeito: she.", "He, she e it usam is: She is a doctor.", "He is a nurse. It is cold."),
    choose("Complete:\nMy friends ___ at the beach.", 2, listOf(
        wrong("is", "My friends é plural (vários amigos). Is é para um só.", "Plural usa are", ErrorKind.Formula),
        wrong("am", "Am é exclusivo do I.", "Am só com I", ErrorKind.Formula),
        correct("are", "Plural funciona como they: are.")
    ), "My friends = eles (they).", "They usa are: My friends are at the beach.", "The kids are tired."),
    choose("Qual é a tradução certa de \"Eu tenho 18 anos\"?", 0, listOf(
        correct("I am 18 years old.", "Em inglês, idade usa to be: você \"é\" 18 anos de idade."),
        wrong("I have 18 years.", "Tradução palavra por palavra. Em inglês, idade se diz com to be.", "Idade com to be")
    ), "Idade em inglês usa to be, não have.", "I am 18 (years old).", "Ela tem 30 anos → She is 30.")
)

internal fun Curriculum.englishToBeQuestions() = lesson(
    "en-to-be-questions", "To be: negativa e pergunta", "Negue com not e pergunte invertendo o verbo.",
    "Com o verbo to be não precisa de verbo auxiliar. Para negar, coloque not depois de am, is ou are. Para perguntar, troque a ordem: o verbo vem antes do sujeito.",
    "You are tired → You are not tired (negativa) → Are you tired? (pergunta).",
    "Negativa:\\ Sujeito+am/is/are+not\\quad Pergunta:\\ Am/Is/Are+sujeito+?",
    "not = não. Na pergunta o verbo pula para a frente do sujeito. Formas curtas: isn't = is not, aren't = are not, I'm not = I am not.",
    "Respostas curtas repetem o verbo: Are you ready? Yes, I am. / No, I'm not. Is she here? Yes, she is. / No, she isn't. Não existe \"amn't\": use I'm not.",
    Diagram.Sentence, "Na pergunta, o bloco do verbo troca de lugar com o sujeito.",
    "Negar: verbo + not. Perguntar: verbo antes do sujeito.",
    "Para perguntar se alguém está bem (Are you OK?), se algo está aberto (Is it open?) ou negar algo (It isn't expensive).",
    englishSources, listOf("en-to-be"),
    choose("Transforme em pergunta:\nShe is at work. → ___", 1, listOf(
        wrong("She is at work?", "Na escrita, a ordem precisa mudar. Só a entonação não basta.", "Inversão na pergunta", ErrorKind.Formula),
        correct("Is she at work?", "O verbo is vai para antes do sujeito."),
        wrong("Does she is at work?", "Com to be não se usa do/does.", "To be não usa auxiliar", ErrorKind.Formula)
    ), "Ache o verbo: is.", "Coloque is antes de she: Is she at work?", "They are late → Are they late?"),
    choose("Negue:\nI am hungry. → ___", 2, listOf(
        wrong("I amn't hungry.", "A contração amn't não existe.", "Contração de am not", ErrorKind.Formula),
        wrong("I don't hungry.", "Don't é auxiliar de outros verbos. Com to be, use not.", "To be não usa auxiliar", ErrorKind.Formula),
        correct("I'm not hungry.", "I am not → I'm not.")
    ), "Coloque not depois de am.", "Contraia I am para I'm: I'm not hungry.", "He is busy → He isn't busy.")
)

internal fun Curriculum.englishPossessives() = lesson(
    "en-possessives", "Possessivos: my, your, his, her", "Diga de quem é algo.",
    "O possessivo vem antes da coisa e muda conforme o dono, não conforme a coisa. Em português \"seu carro\" e \"sua casa\" mudam. Em inglês é sempre \"his car\" e \"his house\" se o dono é ele.",
    "Pedro mora com a irmã. His sister is a nurse (A irmã dele é enfermeira). Her name is Ana (O nome dela é Ana).",
    "Possessivo+coisa",
    "I → my. You → your. He → his. She → her. It → its. We → our. They → their.",
    "Não confunda its (dele, de coisa) com it's (it is). Para nomes, use 's: Pedro's car = o carro do Pedro. Their (deles), there (lá) e they're (they are) soam iguais e são diferentes.",
    Diagram.Sentence, "O possessivo é um bloco grudado antes da coisa: my + phone.",
    "O possessivo combina com o dono. A coisa não muda nada.",
    "Para falar da família, das coisas e de contatos: My phone number is…, Her email is…",
    englishSources, listOf("en-pronouns"),
    choose("Complete (o dono é o Lucas):\nLucas loves ___ mother.", 0, listOf(
        correct("his", "O dono é ele (Lucas): his."),
        wrong("her", "Her seria se a dona fosse uma mulher. A mãe é mulher, mas o dono é o Lucas.", "Possessivo segue o dono"),
        wrong("he", "He é sujeito. Antes de um nome, use o possessivo.", "Sujeito e possessivo", ErrorKind.Formula)
    ), "Pergunte: a mãe é de quem? Do Lucas, um homem.", "Homem dono → his: Lucas loves his mother.", "Ana loves her father."),
    choose("Complete:\nWe are proud of ___ team.", 2, listOf(
        wrong("we", "We é sujeito. Antes de um nome, use o possessivo.", "Sujeito e possessivo", ErrorKind.Formula),
        wrong("their", "Their é deles. O time é nosso.", "Possessivo segue o dono"),
        correct("our", "We → our (nosso).")
    ), "O dono é we (nós).", "We → our: We are proud of our team.", "They love their dog.")
)

internal fun Curriculum.englishArticles() = lesson(
    "en-articles", "Artigos: a, an, the", "Escolha entre a, an e the.",
    "A e an significam um ou uma, para algo qualquer e no singular. The significa o, a, os, as, para algo específico. A escolha entre a e an depende do som da próxima palavra, não da letra.",
    "I have a car (um carro qualquer). The car is red (o carro de que já falei). I eat an apple (an porque apple começa com som de vogal).",
    "a/an+coisa\\ qualquer\\quad the+coisa\\ específica",
    "a = antes de som de consoante. an = antes de som de vogal. the = específico, singular ou plural.",
    "O que vale é o som: an hour (o h é mudo), a university (soa \"iu\"). Profissões levam a/an: She is a doctor. Plural genérico fica sem artigo: Dogs are friendly.",
    Diagram.Sentence, "O artigo é um bloco pequeno antes da coisa.",
    "Um qualquer: a/an. O específico: the. An antes de som de vogal.",
    "Pedir algo numa loja (a coffee, please), falar da profissão (I am a developer) e apontar coisas (the bus, the station).",
    englishSources, listOf("en-pronouns"),
    choose("Complete:\nShe is ___ engineer.", 1, listOf(
        wrong("a", "Engineer começa com som de vogal. Antes de som de vogal, use an.", "A e an pelo som", ErrorKind.Formula),
        correct("an", "Som de vogal no início: an engineer."),
        wrong("the", "Profissão leva a/an, não the.", "Artigo com profissão")
    ), "Ouça o começo: \"én-djinír\", som de vogal.", "Som de vogal → an: She is an engineer.", "He is a teacher."),
    choose("Complete (você já falou do filme antes):\nI watched a movie. ___ movie was great.", 2, listOf(
        wrong("A", "A apresenta algo novo. O filme já foi mencionado.", "Algo novo e algo específico"),
        wrong("An", "An também é para algo novo, e movie começa com som de consoante.", "Algo novo e algo específico"),
        correct("The", "O filme já é conhecido: the.")
    ), "O filme já apareceu na frase anterior.", "Algo específico → the: The movie was great.", "I bought a shirt. The shirt is blue.")
)

internal fun Curriculum.englishThereIs() = lesson(
    "en-there-is", "There is e there are", "Diga que existe ou que há alguma coisa.",
    "There is e there are significam \"há\" ou \"tem\" no sentido de existir. Use there is para uma coisa e there are para várias.",
    "There is a bank near here (Tem um banco aqui perto). There are two cats in the house (Há dois gatos na casa).",
    "There+is/are+coisa+lugar",
    "There is = há (singular). There are = há (plural). A coisa vem depois do verbo. O lugar costuma vir no fim.",
    "Não use \"have\" para existir: \"Tem um mercado aqui\" é There is a market here, não Have a market here. Pergunta: Is there…? / Are there…? Negativa: There isn't… / There aren't…",
    Diagram.Sentence, "O bloco there + is/are anuncia que algo existe.",
    "Uma coisa: there is. Várias: there are. Perguntar: Is there…? Are there…?",
    "Para perguntar se há um banheiro, uma farmácia ou wi-fi: Is there a pharmacy near here?",
    englishSources, listOf("en-to-be", "en-articles"),
    choose("Complete:\n___ three people in the room.", 1, listOf(
        wrong("There is", "Three people é plural.", "There is e there are", ErrorKind.Formula),
        correct("There are", "Várias pessoas: there are."),
        wrong("Have", "Have é ter (posse). Para existir, use there is/are.", "Existir e possuir")
    ), "Conte: três pessoas, plural.", "Plural → there are.", "There is one chair."),
    choose("Como perguntar \"Tem wi-fi aqui?\"", 0, listOf(
        correct("Is there wi-fi here?", "Pergunta: inverta para Is there."),
        wrong("Have wi-fi here?", "Have não indica existência e a pergunta precisa de sujeito.", "Existir e possuir"),
        wrong("There is wi-fi here?", "Na pergunta, is vem antes de there.", "Inversão na pergunta", ErrorKind.Formula)
    ), "Comece com There is.", "Inverta para perguntar: Is there wi-fi here?", "Are there any tables free?")
)

internal fun Curriculum.englishPresent() = lesson(
    "en-present", "Presente simples: I work, she works", "Fale de rotinas e coloque o -s com he, she e it.",
    "O presente simples fala de hábitos, rotinas e verdades. O verbo fica igual para quase todos. A única mudança: com he, she, it (ou um nome no singular) o verbo ganha -s.",
    "I work every day. She works every day. They play soccer. He plays soccer.",
    "Sujeito+verbo(+s\\ com\\ he/she/it)+complemento",
    "I, you, we, they → verbo normal. He, she, it → verbo + s. Verbos terminados em o, s, sh, ch, x ganham -es: go → goes, watch → watches. Consoante + y vira -ies: study → studies.",
    "Irregular importante: have → has (She has a car). Palavras de frequência vêm antes do verbo: I always wake up early, She never drinks coffee.",
    Diagram.Sentence, "O verbo é o bloco do meio. Com he, she e it ele ganha um -s no fim.",
    "He, she, it: coloque -s. Os outros: verbo puro.",
    "Para contar a sua rotina, o que você faz no trabalho ou do que gosta: I work with software. She lives in São Paulo.",
    englishSources, listOf("en-pronouns"),
    choose("Complete:\nMy brother ___ in Rio.", 1, listOf(
        wrong("live", "My brother = he. Com he, o verbo ganha -s.", "S da terceira pessoa", ErrorKind.Formula),
        correct("lives", "He → lives."),
        wrong("is live", "Não se junta is com outro verbo no presente simples.", "To be e verbo de ação", ErrorKind.Formula)
    ), "My brother = he.", "He → verbo + s: lives.", "My parents live in Rio."),
    choose("Complete:\nShe ___ English at night.", 2, listOf(
        wrong("study", "She pede o -s.", "S da terceira pessoa", ErrorKind.Formula),
        wrong("studys", "Consoante + y vira -ies.", "Ortografia do -s", ErrorKind.Formula),
        correct("studies", "Study termina em consoante + y: studies.")
    ), "She pede -s.", "Study → studies (y vira ies).", "He tries, she cries.")
)

internal fun Curriculum.englishDoDoes() = lesson(
    "en-do-does", "Do e does: negativa e pergunta", "Negue e pergunte com verbos de ação.",
    "Com verbos de ação (work, like, live…) o inglês precisa de um ajudante: do ou does. Use does com he, she, it e do com os outros. Quando does aparece, ele pega o -s e o verbo principal volta ao normal.",
    "She works → She doesn't work → Does she work? / You like pizza → You don't like pizza → Do you like pizza?",
    "Do/Does+sujeito+verbo+?\\quad Sujeito+don't/doesn't+verbo",
    "do = auxiliar para I, you, we, they. does = auxiliar para he, she, it. don't = do not. doesn't = does not. O verbo depois do auxiliar fica sem -s.",
    "Do e does não têm tradução: só marcam pergunta ou negação. Resposta curta: Do you speak English? Yes, I do. / No, I don't. Não use do com to be: Are you tired?, não Do you are tired?",
    Diagram.Sentence, "O auxiliar entra como um bloco novo. O -s sai do verbo e vai para o does.",
    "Pergunta: Do/Does + sujeito + verbo. Negativa: don't/doesn't + verbo. Nunca use -s depois de does.",
    "Para perguntar o que a pessoa gosta (Do you like…?), onde mora (Where do you live?) e dizer o que não faz (I don't smoke).",
    englishSources, listOf("en-present", "en-to-be-questions"),
    choose("Complete:\n___ he speak Spanish?", 1, listOf(
        wrong("Do", "Com he, o auxiliar é does.", "Do e does", ErrorKind.Formula),
        correct("Does", "He → does."),
        wrong("Is", "Speak é verbo de ação, precisa de do/does.", "To be e verbo de ação", ErrorKind.Formula)
    ), "Speak é verbo de ação: precisa de auxiliar.", "He → does: Does he speak Spanish?", "Do they speak Spanish?"),
    choose("Negue:\nShe likes coffee. → She ___ coffee.", 0, listOf(
        correct("doesn't like", "Doesn't já tem o -s, então o verbo volta para like."),
        wrong("doesn't likes", "O -s já está em doesn't. O verbo fica sem -s.", "S vai para o auxiliar", ErrorKind.Formula),
        wrong("don't like", "Com she, o auxiliar é does.", "Do e does", ErrorKind.Formula)
    ), "She → doesn't.", "Depois de doesn't o verbo fica puro: doesn't like.", "He doesn't play. They don't play.")
)

internal fun Curriculum.englishContinuous() = lesson(
    "en-continuous", "Presente contínuo: I am working", "Diga o que está acontecendo agora.",
    "O presente contínuo descreve algo em andamento neste momento, como o nosso \"estou fazendo\". Junte o to be certo com o verbo terminado em -ing.",
    "I am studying now (Estou estudando agora). She is talking on the phone. They are playing.",
    "Sujeito+am/is/are+(verbo+ing)",
    "am/is/are segue a regra do to be. verbo + ing = fazendo. Verbos terminados em e perdem o e: make → making. Vogal curta + consoante dobra: run → running.",
    "Compare: I work every day (rotina, presente simples) e I am working now (agora, contínuo). Pergunta: Are you working? Negativa: I'm not working.",
    Diagram.Sentence, "Dois pedaços de verbo: o to be e o verbo com -ing.",
    "Agora, neste momento: am/is/are + verbo-ing.",
    "Para responder What are you doing? (O que você está fazendo?) por mensagem ou ligação.",
    englishSources, listOf("en-to-be", "en-present"),
    choose("Complete:\nLook! It ___.", 2, listOf(
        wrong("rain", "Look! indica agora. Falta to be + -ing.", "Agora e rotina", ErrorKind.Formula),
        wrong("raining", "Falta o to be: it is raining.", "To be no contínuo", ErrorKind.Formula),
        correct("is raining", "It + is + raining.")
    ), "Look! mostra que é agora.", "It → is, e o verbo com -ing: is raining.", "They are running."),
    choose("Qual frase fala de AGORA?", 0, listOf(
        correct("I am cooking dinner.", "To be + -ing: está acontecendo agora."),
        wrong("I cook dinner every day.", "Every day indica rotina, presente simples.", "Agora e rotina")
    ), "Procure to be + -ing.", "I am cooking = estou cozinhando agora.", "She is sleeping now.")
)

internal fun Curriculum.englishObjectPronouns() = lesson(
    "en-object-pronouns", "Pronomes objeto: me, him, her", "Use o pronome certo depois do verbo.",
    "Quando a pessoa recebe a ação, o pronome muda. Antes do verbo (quem faz) é I, he, she. Depois do verbo ou de preposição (quem recebe) é me, him, her.",
    "She loves him (Ela o ama). He calls me every day (Ele me liga todo dia). Talk to them (Fale com eles).",
    "Sujeito+verbo+pronome\\ objeto",
    "I → me. You → you. He → him. She → her. It → it. We → us. They → them.",
    "A ordem é diferente do português: \"Ele me ama\" vira He loves me, com o me depois do verbo. Depois de preposição também: with me, for her, to them.",
    Diagram.Sentence, "O pronome objeto é o último bloco, depois do verbo.",
    "Quem faz: I, he, she. Quem recebe: me, him, her, us, them.",
    "Para pedir ajuda (Help me, please), mandar mensagem (Call me later) ou falar de alguém (I like her).",
    englishSources, listOf("en-present", "en-possessives"),
    choose("Traduza \"Ela me ajuda\":", 1, listOf(
        wrong("She me helps.", "Em inglês o objeto vem depois do verbo.", "Ordem do objeto", ErrorKind.Formula),
        correct("She helps me.", "Sujeito + verbo + objeto."),
        wrong("She helps I.", "I é sujeito. Depois do verbo, use me.", "Sujeito e objeto", ErrorKind.Formula)
    ), "Monte: She + helps + objeto.", "Eu como objeto = me: She helps me.", "We visit them."),
    choose("Complete:\nThis gift is for ___ (para ela).", 2, listOf(
        wrong("she", "She é sujeito. Depois de for, use objeto.", "Sujeito e objeto", ErrorKind.Formula),
        wrong("his", "His é possessivo de um homem.", "Possessivo e objeto"),
        correct("her", "Depois de preposição: her.")
    ), "For é preposição: pede pronome objeto.", "Ela → her: for her.", "Come with us.")
)

internal fun Curriculum.englishCan() = lesson(
    "en-can", "Can e can't", "Diga o que você consegue fazer e faça pedidos.",
    "Can significa poder ou conseguir. É o mesmo para todos os sujeitos e nunca ganha -s. O verbo depois dele fica puro, sem to.",
    "I can swim (Eu sei nadar). She can't drive (Ela não sabe dirigir). Can you help me? (Você pode me ajudar?)",
    "Sujeito+can/can't+verbo",
    "can = posso, consigo, sei. can't = cannot = não posso. O verbo depois de can fica na forma básica.",
    "Pergunta: inverta. Can I come in? (Posso entrar?). Para pedidos mais educados use could: Could you repeat, please?",
    Diagram.Sentence, "Can entra antes do verbo e não muda com o sujeito.",
    "Can + verbo puro. Nada de -s, nada de to.",
    "Para pedir algo (Can I have the bill?), oferecer ajuda (I can help you) e dizer habilidades numa entrevista (I can speak Portuguese).",
    englishSources, listOf("en-present"),
    choose("Complete:\nHe ___ English very well.", 0, listOf(
        correct("can speak", "Can + verbo puro."),
        wrong("cans speak", "Can nunca ganha -s.", "Can não muda", ErrorKind.Formula),
        wrong("can speaks", "Depois de can, o verbo fica sem -s.", "Verbo puro depois de can", ErrorKind.Formula),
        wrong("can to speak", "Depois de can não se usa to.", "Verbo puro depois de can", ErrorKind.Formula)
    ), "Can é igual para todos.", "Verbo puro depois: He can speak English.", "She can dance."),
    choose("Como pedir \"Posso pagar com cartão?\"", 1, listOf(
        wrong("I can pay by card?", "Na pergunta, can vem antes do sujeito.", "Inversão na pergunta", ErrorKind.Formula),
        correct("Can I pay by card?", "Can antes do sujeito: pergunta."),
        wrong("Do I can pay by card?", "Can não usa do.", "Can não usa auxiliar", ErrorKind.Formula)
    ), "Comece com I can pay by card.", "Inverta: Can I pay by card?", "Can you open the window?")
)

internal fun Curriculum.englishWh() = lesson(
    "en-wh", "Perguntas: what, where, when, who, why, how", "Faça perguntas abertas.",
    "As palavras de pergunta vêm no começo. Depois vem a pergunta normal de sim ou não: o verbo to be invertido ou do/does.",
    "Where do you live? (Onde você mora?) When is the party? (Quando é a festa?) Why are you sad? (Por que você está triste?)",
    "Wh+do/does\\ ou\\ to\\ be+sujeito+?",
    "What = o que, qual. Where = onde. When = quando. Who = quem. Why = por quê. How = como. How much = quanto (preço). How many = quantos. How old = quantos anos.",
    "Why pergunta, because responde: Why are you late? Because the bus was late. Who como sujeito não usa do: Who lives here?",
    Diagram.Sentence, "A palavra de pergunta entra na frente de uma pergunta comum.",
    "Wh + pergunta comum: Where + do you live?",
    "Para pedir direções (Where is the station?), saber horários (What time is it?) e preços (How much is it?).",
    englishSources, listOf("en-do-does", "en-to-be-questions"),
    choose("Qual palavra pergunta QUANDO?\n___ is your birthday?", 1, listOf(
        wrong("Where", "Where pergunta lugar.", "Palavras de pergunta"),
        correct("When", "When = quando."),
        wrong("Who", "Who pergunta pessoa.", "Palavras de pergunta"),
        wrong("Why", "Why pergunta motivo.", "Palavras de pergunta")
    ), "Aniversário é uma data.", "Data ou momento → when.", "When does the class start?"),
    choose("Monte a pergunta \"Onde ela trabalha?\"", 2, listOf(
        wrong("Where she works?", "Falta o auxiliar does.", "Auxiliar na pergunta", ErrorKind.Formula),
        wrong("Where does she works?", "Depois de does o verbo fica sem -s.", "S vai para o auxiliar", ErrorKind.Formula),
        correct("Where does she work?", "Where + does + she + work.")
    ), "Pergunta comum: Does she work?", "Coloque where na frente: Where does she work?", "What do you do?")
)

internal fun Curriculum.englishTime() = lesson(
    "en-time", "Preposições de tempo: in, on, at", "Diga quando com in, on e at.",
    "Pense numa pirâmide: in para períodos grandes, on para dias e at para horários exatos.",
    "I was born in 2005, in May. My class is on Monday. The movie starts at 8 pm.",
    "in\\ (mês,\\ ano)\\quad on\\ (dia)\\quad at\\ (hora)",
    "in = meses, anos, estações e partes do dia (in the morning). on = dias da semana e datas (on Friday, on July 4th). at = horas e momentos (at 7 o'clock, at night, at noon).",
    "Exceções comuns: at night (à noite), at the weekend no inglês britânico e on the weekend no americano. Com this, next e last não se usa preposição: next Monday.",
    Diagram.Sentence, "Do maior para o menor: in, on, at.",
    "Grande: in. Dia: on. Hora: at.",
    "Para marcar reuniões, consultas e encontros: See you on Friday at 3 pm.",
    englishSources, listOf("en-wh"),
    choose("Complete:\nThe meeting is ___ Tuesday.", 0, listOf(
        correct("on", "Dia da semana: on."),
        wrong("in", "In é para mês e ano.", "In, on e at"),
        wrong("at", "At é para hora exata.", "In, on e at")
    ), "Tuesday é um dia.", "Dia → on: on Tuesday.", "on Sunday, on my birthday."),
    choose("Complete:\nI wake up ___ 6 o'clock.", 2, listOf(
        wrong("in", "In é para períodos grandes.", "In, on e at"),
        wrong("on", "On é para dias.", "In, on e at"),
        correct("at", "Hora exata: at.")
    ), "6 o'clock é uma hora.", "Hora → at: at 6 o'clock.", "at noon, at midnight.")
)

internal fun Curriculum.englishWasWere() = lesson(
    "en-was-were", "Passado do to be: was e were", "Diga como algo era ou estava.",
    "No passado, o to be tem só duas formas: was para I, he, she, it e were para you, we, they.",
    "I was tired yesterday (Eu estava cansado ontem). They were at the party (Eles estavam na festa).",
    "Sujeito+was/were+complemento",
    "was = I, he, she, it, singular. were = you, we, they, plural. Negativa: wasn't, weren't. Pergunta: Was she…? Were you…?",
    "Também serve para \"nasci\": I was born in 2004. Marcadores de passado: yesterday, last week, ago (two days ago).",
    Diagram.Sentence, "O verbo do meio muda para a forma do passado.",
    "I, he, she, it → was. You, we, they → were.",
    "Para contar como foi o dia (It was great!), onde você estava e quando nasceu.",
    englishSources, listOf("en-to-be-questions"),
    choose("Complete:\nWe ___ at home last night.", 1, listOf(
        wrong("was", "We usa were.", "Was e were", ErrorKind.Formula),
        correct("were", "We → were."),
        wrong("are", "Last night pede passado.", "Presente e passado", ErrorKind.Formula)
    ), "Last night = passado.", "We → were.", "I was at home."),
    choose("Como dizer \"Eu nasci em 2004\"?", 0, listOf(
        correct("I was born in 2004.", "Nascer em inglês é be born: I was born."),
        wrong("I born in 2004.", "Falta o was. Nascer usa to be: was born.", "Be born"),
        wrong("I am born in 2004.", "Nascimento é passado: was.", "Presente e passado", ErrorKind.Formula)
    ), "Nascer em inglês é be born.", "No passado: I was born in 2004.", "She was born in May.")
)

internal fun Curriculum.englishPast() = lesson(
    "en-past", "Passado simples: worked, went, did", "Conte o que aconteceu.",
    "No passado simples, verbos regulares ganham -ed e servem para todos os sujeitos. Os irregulares têm forma própria que precisa ser decorada. Para negar e perguntar, use did, e o verbo volta para a forma básica.",
    "I worked yesterday. She went to Paris. Did you go? I didn't go.",
    "Sujeito+(verbo+ed)\\ ou\\ irregular\\quad Did+sujeito+verbo+?",
    "Regular: work → worked, play → played, study → studied. Irregulares comuns: go → went, have → had, do → did, see → saw, eat → ate, get → got, make → made, say → said. did = auxiliar do passado. didn't = did not.",
    "Depois de did, o verbo fica puro: Did she go? (nunca Did she went?). O passado é igual para todos os sujeitos, sem -s.",
    Diagram.Sentence, "O verbo muda para o passado. Na pergunta, did assume o passado.",
    "Afirmativa: verbo no passado. Pergunta e negativa: did + verbo puro.",
    "Para contar como foi o fim de semana, uma viagem ou o que você fez no trabalho: I finished the project.",
    englishSources, listOf("en-do-does", "en-was-were"),
    choose("Complete:\nYesterday I ___ to the gym.", 2, listOf(
        wrong("go", "Yesterday pede passado.", "Presente e passado", ErrorKind.Formula),
        wrong("goed", "Go é irregular. O passado é went.", "Verbos irregulares", ErrorKind.Formula),
        correct("went", "Go → went.")
    ), "Yesterday indica passado.", "Go é irregular: went.", "She saw a movie."),
    choose("Monte a pergunta \"Você comeu?\"", 0, listOf(
        correct("Did you eat?", "Did + sujeito + verbo puro."),
        wrong("Did you ate?", "O passado já está no did. O verbo fica puro.", "Verbo puro depois de did", ErrorKind.Formula),
        wrong("Do you ate?", "Do é presente, e ate não vai depois de auxiliar.", "Did no passado", ErrorKind.Formula)
    ), "Pergunta no passado: comece com did.", "Verbo puro: Did you eat?", "Did they call you?")
)

internal fun Curriculum.englishFuture() = lesson(
    "en-future", "Futuro: will e going to", "Fale de planos e previsões.",
    "Will serve para decisões na hora, promessas e previsões. Going to serve para planos já decididos. Nos dois casos o verbo fica puro.",
    "I will help you (Eu vou te ajudar, decidi agora). I am going to travel next month (Vou viajar mês que vem, já planejei).",
    "Sujeito+will+verbo\\quad Sujeito+am/is/are+going\\ to+verbo",
    "will = vou, vai (igual para todos). won't = will not. going to = vou, vai, com o to be certo antes.",
    "Na fala: I'll, you'll, she'll. Going to vira \"gonna\" informalmente: I'm gonna call you. Pergunta: Will you…? Are you going to…?",
    Diagram.Sentence, "Will ou going to entram antes do verbo puro.",
    "Plano já decidido: going to. Decisão na hora, promessa, previsão: will.",
    "Para combinar planos (We are going to meet at 7) e prometer algo (I'll send it tomorrow).",
    englishSources, listOf("en-continuous", "en-can"),
    choose("Complete:\nShe ___ visit her mom next week. (já está planejado)", 1, listOf(
        wrong("will to", "Will não usa to.", "Verbo puro depois de will", ErrorKind.Formula),
        correct("is going to", "Plano decidido: is going to."),
        wrong("going to", "Falta o to be: she is going to.", "To be no going to", ErrorKind.Formula)
    ), "É um plano já decidido.", "She → is going to visit.", "They are going to move."),
    choose("O telefone toca. Você decide na hora:\n\"___ answer it!\"", 0, listOf(
        correct("I'll", "Decisão no momento: will."),
        wrong("I'm going to", "Going to é para planos feitos antes.", "Will e going to"),
        wrong("I will to", "Will não usa to.", "Verbo puro depois de will", ErrorKind.Formula)
    ), "A decisão é na hora.", "Decisão espontânea → I'll answer it!", "I'll call you later.")
)
