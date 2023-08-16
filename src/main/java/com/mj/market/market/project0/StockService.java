package com.mj.market.market.project0;


public class StockService {/*
    //implements PriceAlertObserver

    private final PriceAlertRepository priceAlertRepository;
    private final EmailService emailService;
    private final StockTickerService stockTickerService;
    private List<PriceAlert> cachePriceAlertList = new LinkedList<>();
    private boolean isCurrentAlertList = false;
    private static short loopDelayCounter =0;
    private final boolean activeSendNotification = true;


    @Autowired
    public StockService(PriceAlertRepository priceAlertRepository, EmailService emailService, StockTickerService stockTickerService) {
        this.priceAlertRepository = priceAlertRepository;
        this.emailService = emailService;
        this.stockTickerService = stockTickerService;
    }

    public StockApiWrapper findStock(final StockTicker ticker){
        try{
            return StockApiWrapper.getInstance(ticker.getSymbol());
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    public StockApiWrapper findStock(final String ticker){
        try{
            return StockApiWrapper.getInstance(ticker);
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private static short startLoopDelayCounter(){
        loopDelayCounter++;
        if(loopDelayCounter >= 3){
            loopDelayCounter=3;
        }
        return loopDelayCounter;
    }

    *//**
    @Scheduled()
    priceAlertCheckLoop()
    Zadanie: Tworzy pętlę do porównywania ceny waloru z giełdy, z ceną alertu dodaną przez użytkownika, w celu wysłania powiadomienia o zmianie.
    Wartość liczbowa zmiennej counter, odzwierciedla aktualny stan pętli i dzieli ją na etapy.
    counter = 0 - 2 - Przebiegi pętli potrzebny na inicjalizacji alertów cachePriceAlertList bezpośrednio z repozytorium priceAlertRepository, oraz zapisanie ich kopii w cache
    counter >= 2    - Rozpoczęcie porównywania cen rynkowych stocksList (z StockService) z cenami cachePriceAlertList
    counter >= 3    - Załadowanie ceny PriceAlert z cache i nasłuchiwanie zmian ceny z PriceAlertService (isActualAlertList = false spowoduje załadowanie cen z priceAlertRepository)
     **//*

    @Scheduled(fixedRate = 30000) //60 000 - 1min
    public void priceAlertCheckLoop() throws IOException {
        int LOOP_DELAY_REQUIRE_TO_LOAD = 3;
        int LOOP_DELAY_REQUIRE_TO_COMPARE = 2;
        short counter = startLoopDelayCounter();

        if(counter < LOOP_DELAY_REQUIRE_TO_LOAD){
            cachePriceAlertList = readPriceAlertList();
        }else{
            cachePriceAlertList = loadPriceAlertList();
        }

        if(counter >= LOOP_DELAY_REQUIRE_TO_COMPARE){
            showAlertsList(cachePriceAlertList);
            Set<String> tickersSet = getDistinctTickersFromAlertList(cachePriceAlertList);
            List<StockApiWrapper> stocksList = loadStocks(tickersSet);
            comparePricesAndSentNotifications(stocksList,cachePriceAlertList);
        }
    }



    private void sendNotification(List<PriceAlert> toBeSendList, Map<Long, BigDecimal> currentPrices){
        if(!toBeSendList.isEmpty()){
            for (PriceAlert element: toBeSendList){
                BigDecimal currentPrice;
                PriceAlert repoAlert = priceAlertRepository.findById(element.getId()).get();
                    if(repoAlert != null){
                        currentPrice = currentPrices.get(repoAlert.getId());
                        repoAlert.setActive(false);
                        priceAlertRepository.save(repoAlert);
                        sendPriceAlertToUser(repoAlert, currentPrice);
                };
            }
        }
        setNotCurrentPriceAlertsList();
    }

    private void sendPriceAlertToUser(PriceAlert priceAlert, BigDecimal currentPrice){
        emailService.send(priceAlert, currentPrice);
    }

    private Set<String> getDistinctTickersFromAlertList(List<PriceAlert> cachePriceAlertList) {
        Set<String> set = new HashSet<>();
        for(PriceAlert alert: cachePriceAlertList){
            if(!set.contains(alert.getTicker())){
                set.add(alert.getTicker().getSymbol());
            }
        }
        return set;
    }

    private void showAlertsList(List<PriceAlert> list){
        for(PriceAlert alert: list){
                System.out.println(alert.getId() +": "+alert.getTicker() +", max: "+ alert.getMaxPrice() +" ,min: "+ alert.getMinPrice() +" ,active=: "+ alert.getActive());
        }
    }

    public List<StockApiWrapper> findAllStocks() {
        return stockTickerService.getAllStockTickers()
                .stream()
                .map(ticker -> findStock(ticker))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<StockApiWrapper> loadStocks(Set<String> tickers) {
        return tickers.stream()
                        .map((String ticker) -> findStock(ticker))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }

    private List<PriceAlert> readPriceAlertList(){
        cachePriceAlertList = priceAlertRepository.findByIsActive(true); //findAll();
        isCurrentAlertList = true;
       System.out.println("+++++++++ readPriceAlertList()  isActualAlertList = true; ");
        return cachePriceAlertList;
     }

     private List<PriceAlert> loadPriceAlertList() {
         if (!isCurrentAlertList) {
             return readPriceAlertList();
         }
        System.out.println("+++++++++ loadPriceAlertList() from Cache");
         return cachePriceAlertList;
     }

    @Override
    public void setNotCurrentPriceAlertsList() {
        isCurrentAlertList = false;
        System.out.println("+++++++++  isCurrentAlertList = false;");
    }*/
}
