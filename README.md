# wheel-of-fortune

SERVER
    Listen to player connections
    when 3 players arrives a game starts
    server also acts as a DB, reading and storing games data

    PROPERTIES
        arrayList[3]
        LinkedList<Games> gamesList = new LinkedList<>;
        ExecutorService gameThreads = Executors.newcachedThreadPool(); 


    METHODS
        listen()
            ServerSocket serverSocket= new ServerSocket(8080);
            Game game=null;
            while(true){
            Socket playerSocket = serverSocket.accept()
           
            
            if (game==null) { 
                Game game = new Game();
                gameThreads.submit(game)
                    game.run();começa o processo
            }
           
            if(!game.isFull()){
                    game.addPlayer(playerSocket)
                    game->sendMessage(welcome);
                    game->sendMessasge(input your name);
                    game->sendMessage(waiting for players to join )
            }

            if(game.isFull()){   
                game=null;
            }
        }

        GeneratorStringToGame();
        



GAME
    Player[] players 
    int[] playersScore

    START()
    SPINWHEEL()
    askForPhrase()
    games starts
    game aks server a phrase (game replace the words for #)
    game broadcasts the phrase to all players

    
    Process
    player x spins the wheel and gets a money value, a free play, a miss turn or bankrupt
    All players can see the wheel spinning and the result
    if player gets bankrupt he loses all the money
    if player gets miss turn go to next player
    if player gets free play he can play twice
    game asks player for consoant or to buy voal or to tell the phrase;
    games checks if consoant is present on phrase
    if player finds a consoant present in phrase it earns the money amount 
    game broadcast the phrase with uncovered result
    games tell to player x+1 to spin the wheel;
    repeat the process again;


    Player console user journey
    Welcome to MindSwap's Wheels of Fortune
    Please input your name
        Joaquim
    Please wait while other players join the game
    Game has started. you're playing against Teresa and Mário
    Spin the wheel - (enter a number between 5 and 10 to simbolize the amount of force to spin)
        /spin
    [110|bankrupt|300|miss turn|500|1000|bankrupt|500|1000|miss turn|free play]
    [110|bankr]
    [10|bankru]
    [0|bankrup]

    Option value
    You've got 500 on the wheel
    Choose buy a vowel, choose a consoant or guess phrase
    Path 1
        /buy a
    Path 2
        /choose e
    Path 3
        /guess flowers are red violets are blue
    
    Option miss turn
        Oh no you will not play this turn

    Option BankRupt
        Oh no you lose all your money and you miss the turn!
    
    Option FreePlay
        You can play twice!
        Choose buy a vowel, choose a consoant or guess phrase!


  --------------------------------------------  

PLAYER
    //Representa o utilizador, cada player tem um Client,
    
    public Player(name, socket)
    Socket playerSocket = new Socket();
    String name
    Bio

    bufferedReader
    PrintWriter



 
    
    
   
    
    
        



    





