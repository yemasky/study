
app.controller('RoomOrderController', function($rootScope, $scope, $httpService, $location, $translate, $aside, $ocLazyLoad, $alert, $filter) {
    "use strict"; 
	$scope.loading.start();$scope.param = {};
	var bookRoomFather_id = 0, isBookRoom = false; $scope.bookRoom_quantity = '数量';$scope.param.isBookRoom = 0;
	if(angular.isDefined($scope.bookRoom) && $scope.bookRoom !== '') {//预定特定的房间 从房态页点过来
		bookRoomFather_id = $scope.bookRoom.item_father_id;isBookRoom = true;$scope.bookRoom_quantity = '房号';$scope.param.isBookRoom = 1;//单独预定一个房间
	}
    //日历部分
    $ocLazyLoad.load([$scope._resource + "vendor/libs/daterangepicker.css",$scope._resource + "styles/booking.css",
                      $scope._resource + "vendor/modules/angular-ui-select/select.min.css"]);
    $ocLazyLoad.load([$scope._resource + "vendor/modules/angular-ui-select/select.min.js"]);
    $scope.booking_room = {};$scope.booking_price = {}; $scope.system_price = {};
    var priceLayout = {};//房型价格
    //选择客源市场
    $scope.market_name = '散客步入';$scope.market_id = '2';$scope.customer_name = '预订人';
    var _channel = angular.isDefined($scope.getChannelModule(12)) ? $scope.getChannelModule(12).url : $scope.$stateParams.channel;
    $scope.param.mobile_email = '';
    var _view = $scope.$stateParams.view;
    //获取数据
    var param = 'channel='+_channel+'&view='+_view+'&market_id='+$scope.market_id;
    $httpService.post('/app.do?'+param, $scope, function(result){
        $scope.loading.percent();
        if(result.data.success === '0') {
            var message = $scope.getErrorByCode(result.data.code);
            $alert({title: 'Error', content: message, templateUrl: '/modal-warning.html', show: true});
        }
        var common = result.data.common;
        $scope.setCommonSetting(common);
        $scope.setThisChannel('Hotel');
        $(document).ready(function(){
            $scope.bookingRoomList = result.data.item.bookingRoom;//已预订的房间
            $scope.channelItemList = result.data.item.arrayChannelItem;
            var layoutRoom = result.data.item.layoutRoom;//房型房间
            $scope.layoutRoom = result.data.item.layoutRoom;//所有房型的房间
            $scope.setLayoutList();//计算房型列表
            $scope.marketList = result.data.item.marketList;
            //按时间计算空房
            //所有价格类型
            $scope.priceSystemHash = result.data.item.priceSystemHash;
            //设置价格体系市场
            var _thisDay = result.data.item.in_date;
            var _thisTime = $filter('date')($scope._baseDateTime(), 'HH:mm');
            var _nextDay = result.data.item.out_date;
            $scope.param.check_in = _thisDay;$scope.param.check_out = _nextDay;
            $scope.setBookingCalendar(_thisDay, _nextDay);
            //时间控件
            $('.check_in').val(_thisDay);$('.check_out').val(_nextDay);
            $('.check_date').daterangepicker({
                "autoApply": true,"startDate": _thisDay,"endDate": _nextDay,"locale":{"format" : 'YYYY-MM-DD hh:mm'}
            }, function(start, end, label) {
              //console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
                var check_in = start.format('YYYY-MM-DD'), check_out = end.format('YYYY-MM-DD');
                $scope.param.check_in = check_in;$scope.param.check_out = check_out;
                $scope.setBookingCalendar(check_in, check_out);
                $('.check_in').val(check_in);$('.check_out').val(check_out);$scope.checkOrderData();
            });
            $('#customer_ul').mouseover(function(e) {$('#customer_ul').next().show();});
            $scope.param.in_time = _thisDay+'T14:00:00.000Z';$scope.param.out_time = _thisDay+'T12:00:00.000Z';
            //var channel_id = result.data.item.defaultChannel_id;
            $scope.param.channel_id = $rootScope.defaultChannel.channel_id;
            $scope.param.channel_father_id = $rootScope.defaultChannel.channel_father_id;
            $scope.defaultHotel = $rootScope.defaultChannel.channel_name;
            //设置客源市场  
            $scope.selectCustomerMarket($scope.marketList[1].children[2], false);
            //
            var resultPriceLayout = result.data.item.priceLayout;
            $scope.setPriceLayout(resultPriceLayout);//计算价格类型
            $scope.selectMarketLayoutPrice();//设置新价格
            $scope.calculatingAmountOfRoom(result.data.item.bookingRoom);//设置可订房数量
        });
    });
    //选择客人市场
    $scope.selectCustomerMarket = function(market, ajaxRoomForcasting) {
        $scope.marketSystemLayout = {};
        if(angular.isDefined(market)) {
            $scope.market_name = market.market_name;
            $scope.market_id = market.market_id;
            $scope.market_father_id =  market.market_father_id;
            $('#customer_ul').next().hide();
            $scope.customer_name = '预订人';
            if(market.market_father_id == '4') {//判断会员是否正确
                $scope.customer_name = market.market_name;
            }
            $scope.setPriceSystemMarket();
            if(ajaxRoomForcasting == true) {$scope.checkOrderData();}//远期房态及取出客源市场价格 远期房态需要从数据库获取
        }
    };
    //设置房型价格 priceLayout ： 全局变量; 
    $scope.setPriceLayout = function (resultPriceLayout) {
        if(resultPriceLayout != '') {
            for(var i in resultPriceLayout) {
                var channel_id = resultPriceLayout[i].channel_id;
                if(typeof(priceLayout[channel_id]) == 'undefined') {priceLayout[channel_id] = {};}
                //
                var item_id = resultPriceLayout[i].item_id;
                if(typeof(priceLayout[channel_id][item_id]) == 'undefined') {priceLayout[channel_id][item_id] = {};}
                //
                var system_id = resultPriceLayout[i].price_system_id;
                if(typeof(priceLayout[channel_id][item_id][system_id]) == 'undefined') {priceLayout[channel_id][item_id][system_id] = {};}
                //
                var price_date = resultPriceLayout[i].layout_price_date;
                priceLayout[channel_id][item_id][system_id][price_date] = resultPriceLayout[i];
            }
        }
    };
    //设置 layoutList 列出所有酒店房型 筛选 等
    $scope.setLayoutList = function () {
        $scope.layoutList = {};$scope.roomList = {};
        var channelItemList = $scope.channelItemList;var layoutRoom = $scope.layoutRoom;
        var layoutList = {};//房型
        var roomList = {},room_data = {};//房间
        for(var channel_id in channelItemList) {
            var thisItemList = channelItemList[channel_id], thisLayoutRoom = {};
            if(typeof(layoutList[channel_id]) == 'undefined') {layoutList[channel_id] = {};room_data[channel_id] = {};}
            if(typeof(layoutRoom[channel_id]) != 'undefined') thisLayoutRoom = layoutRoom[channel_id];//单个channel_id的所有房型房间
            for(var i in thisItemList) {
                if(thisItemList[i].channel_config == 'room') {//房间 根据房型进行排序
                    roomList[thisItemList[i]['item_id']] = thisItemList[i];
                }
                if(thisItemList[i].channel_config == "layout") {//房型
                    var item_category_id = thisItemList[i].item_id;
                    layoutList[channel_id][item_category_id] = thisItemList[i];//房型数据 数组形式
                    var num = 0;
                    if(typeof(thisLayoutRoom[thisItemList[i]['item_id']]) != 'undefined') {
                        for(var j in thisLayoutRoom[thisItemList[i]['item_id']]) {
                            num++;
                        }//room_data[channel_id][thisItemList[i]['item_id']] = thisItemList[i]['item_id'];
                    }
                    layoutList[channel_id][item_category_id]['room_num'] = num;//每个房型原始房量
                    layoutList[channel_id][item_category_id]['room_reservation'] = num;//可订房间数
                    room_data[channel_id][thisItemList[i]['item_id']] = 0;
                    var select_room_num = [];
					if(isBookRoom) {//预订单独房间
						select_room_num[0] = {};select_room_num[0].value = '0';
						select_room_num[0].room_info = {};select_room_num[0].room_info.item_name = '';
						select_room_num[0]['room_info']['item_category_name'] = '';
						select_room_num[1] = {};select_room_num[1]['value'] = $scope.bookRoom.item_name;
						select_room_num[1]['room_info'] = {};select_room_num[1]['room_info']['item_name'] = $scope.bookRoom.item_name;
                        select_room_num[1]['room_info']['item_id'] = $scope.bookRoom.item_id;
						select_room_num[1]['room_info']['item_category_name'] = thisItemList[i].item_name;//房型名称
					} else {
						for(var j = 0; j <= num; j++) {//房型的房间
							select_room_num[j] = {};select_room_num[j]['room_info'] = {};
							select_room_num[j]['room_info']['item_name'] = '';
							select_room_num[j]['room_info']['item_category_name'] = thisItemList[i].item_name;//房型名称
							select_room_num[j]['value'] = j;
						}
					}
                    layoutList[channel_id][item_category_id]['select_room_num'] = select_room_num;
					layoutList[channel_id][item_category_id]['isBookRoom'] = true;
					if(isBookRoom && thisItemList[i]['item_id'] != bookRoomFather_id) layoutList[channel_id][item_category_id]['isBookRoom'] = false;
                }
            }
        }
        $scope.layoutList = layoutList;$scope.roomList = roomList;//$scope.booking_room = room_data;
    }
    //显示layout
    $scope.showLayout = function(item_category_id, show) {
        $scope.layoutShow[item_category_id] = show;
    }
    //选择市场价格类别 [房型当价格类型比较多的时候 用向下箭头表示]
    $scope.setPriceSystemMarket = function() {
        $scope.marketSystemLayout = {};$scope.layoutShow = {};$scope.layoutSystemMore = {};//多个价格体系
        //marketSystemLayout：市场价格类别[市场有什么价格类别按layout排序]
        var priceSystemHash = $scope.priceSystemHash, marketSystemLayout = {}, k = {}, layoutShow = {}, layoutSystemMore = {};
        for(var system_id in priceSystemHash) {//价格类型
            var channel_ids = angular.fromJson(priceSystemHash[system_id].channel_ids);	//价格类型包含的channel
            var layout_item = angular.fromJson(priceSystemHash[system_id].layout_item);	//价格类型包含的item
            var market_ids = angular.fromJson(priceSystemHash[system_id].market_ids);//价格类型包含的market
            for(var market_id in market_ids) {//历遍market
                if(typeof(marketSystemLayout[market_id]) == 'undefined') marketSystemLayout[market_id] = {};
                if(typeof(layoutSystemMore[market_id]) == 'undefined') layoutSystemMore[market_id] = {};
                for(var channel_id in layout_item) {//历遍layout
                    if(typeof(marketSystemLayout[market_id][channel_id]) == 'undefined') marketSystemLayout[market_id][channel_id] = {};
                    if(typeof(layoutSystemMore[market_id][channel_id]) == 'undefined') layoutSystemMore[market_id][channel_id] = {};
                    for(var item_category_id in layout_item[channel_id]) {//历遍layout
                        layoutShow[item_category_id] = 0;
                        var key = market_id + '-' + channel_id + '-' + item_category_id;
                        var systemKey = system_id + '-' + item_category_id;
                        if(!angular.isDefined(marketSystemLayout[market_id][channel_id][item_category_id])) { 
                            marketSystemLayout[market_id][channel_id][item_category_id] = [];
                            k[key] = 0;
                            layoutSystemMore[market_id][channel_id][item_category_id] = 0;
                        } else {
                            k[key]++;
                            layoutSystemMore[market_id][channel_id][item_category_id] = 1;
                        }
                        marketSystemLayout[market_id][channel_id][item_category_id][k[key]] = priceSystemHash[system_id];
                        //此市场的价格
                    }
                }
            }
        }
        //$scope.selectMarketLayoutPrice();//channel_id, item_category_id, system_id
        $scope.marketSystemLayout = marketSystemLayout;
        $scope.layoutShow = layoutShow;$scope.layoutSystemMore = layoutSystemMore;
    };
    //取出客源市场价格及远期房态
    $scope.checkOrderData = function() {
        if($scope.market_id == '0') {
            $alert({title: 'Error', content: "请选择客源市场！", templateUrl: '/modal-warning.html', show: true});
            return;
        }
        if($scope.param['in_time'].length > 8) $scope.param["in_time"] = $scope.param["in_time"].substr(11,5);
        if($scope.param['out_time'].length > 8) $scope.param["out_time"] = $scope.param["out_time"].substr(11,5);
        $scope.loading.start();
        var market_id = $scope.market_id;
        var param = 'channel='+_channel+'&market_id='+market_id;
        $httpService.header('method', 'checkOrderData');
        $httpService.post('/app.do?'+param, $scope, function(result){
            $scope.loading.percent();
			$httpService.deleteHeader('method');
            if(result.data.success == '0') {
                var message = $scope.getErrorByCode(result.data.code);
                $alert({title: 'Error', content: message, templateUrl: '/modal-warning.html', show: true});
            } else {
                var resultPriceLayout = result.data.item.priceLayout;
                $scope.bookingRoomList = result.data.item.bookingRoom;
                //$scope.marketChannelLayoutPrice = {};
                $scope.setPriceLayout(resultPriceLayout);
                $scope.selectMarketLayoutPrice();//设置新价格
                $scope.calculatingAmountOfRoom(result.data.item.bookingRoom);//设置可订房数量
            }
        })
    }
    //预定日历
    $scope.setBookingCalendar = function(in_date, out_date) {//设置日期
        var check_in = new Date(in_date.replace(/-/g, '/'));var check_in_time = check_in.getTime(); 
        var check_out = new Date(out_date.replace(/-/g, '/'));var check_out_time = check_out.getTime();
        var bookingCalendar = {}, colspan = 4;
        for(var i = check_in_time; i < check_out_time; i += 86400000) {
            var thisDate = new Date(i);var year = thisDate.getFullYear();
            var month = thisDate.getMonth() - 0 + 1; if(month < 10) month = '0'+month;
            var day = thisDate.getDate() - 0; if(day < 10) day = '0'+day;
            //date_key = 年-月-日 2018-01-01
            var date_key = year+'-'+month+'-'+day;var week = $scope.weekday[thisDate.getDay()];
            if(typeof(bookingCalendar[date_key]) == 'undefined') {
                bookingCalendar[date_key] = {};
            }
            bookingCalendar[date_key]['day'] = day;    bookingCalendar[date_key]['week'] = week;
            bookingCalendar[date_key]['month'] = month;bookingCalendar[date_key]['year'] = year;
            colspan++;
        }
        $scope.bookingCalendar = bookingCalendar;$scope.colspan = colspan;
    };	
    //根据预定日历和取出来的远期房态计算每天每个房型的房量
    $scope.calculatingAmountOfRoom = function(bookingRoomList) {
        if(bookingRoomList == '') bookingRoomList = $scope.bookingRoomList;//已预定房间数量
        var layoutList = $scope.layoutList;//房型数据 
        var bookingCalendar = angular.copy($scope.bookingCalendar);//预定日历
        var bookingCategory = {};
        //计算已预定房型数据
        for(var i in bookingRoomList) {
            var bookingRoom = bookingRoomList[i];
            var check_in_time = new Date(bookingRoom.check_in.replace(/-/g, '/')).getTime(); 
            var check_out_time = new Date(bookingRoom.check_out.replace(/-/g, '/')).getTime();
            var channel_id = bookingRoom.channel_id,item_category_id = bookingRoom.item_category_id;
            var book_room_id = bookingRoom.item_id;
            var categoryRoom = layoutList[channel_id][item_category_id].room_num;//全部该房型下房间数
            for(var i = check_in_time; i < check_out_time; i += 86400000) {
                var thisDate = new Date(i);var year = thisDate.getFullYear();
                var month = thisDate.getMonth() - 0 + 1; if(month < 10) month = '0'+month;
                var day = thisDate.getDate() - 0; if(day < 10) day = '0'+day;
                //date_key = 年-月-日 2018-01-01
                var date_key = year+'-'+month+'-'+day;
                if(angular.isUndefined(bookingCategory[channel_id])) bookingCategory[channel_id] = {};
                if(angular.isUndefined(bookingCategory[channel_id][item_category_id])) bookingCategory[channel_id][item_category_id] = {};
                if(angular.isUndefined(bookingCategory[channel_id][item_category_id][date_key])) {
                    bookingCategory[channel_id][item_category_id][date_key] = {};
                    bookingCategory[channel_id][item_category_id][date_key]['book_num'] = 0;
                    bookingCategory[channel_id][item_category_id][date_key]['book_room'] = {};
                }
                bookingCategory[channel_id][item_category_id][date_key]['book_num']++;
                bookingCategory[channel_id][item_category_id][date_key]['book_room'][book_room_id] = book_room_id;
            }
        }
        //计算日期可订房数量
        var channelRoomReservation = {},maxChannelRoomReservation = {};
        for(var channel_id in layoutList) {
            channelRoomReservation[channel_id] = {};maxChannelRoomReservation[channel_id] = {};
            for(var item_category_id in layoutList[channel_id]) {
                var room_num = layoutList[channel_id][item_category_id].room_num;
                channelRoomReservation[channel_id][item_category_id] = {};maxChannelRoomReservation[channel_id][item_category_id] = {};
                maxChannelRoomReservation[channel_id][item_category_id].room_num = room_num;
                for(var date_key in bookingCalendar) {
                    if(angular.isUndefined(channelRoomReservation[channel_id][item_category_id][date_key])) {
                        channelRoomReservation[channel_id][item_category_id][date_key] = {};
                        channelRoomReservation[channel_id][item_category_id][date_key]['room_num'] = room_num;//date_key当天可定全部房量
                    }
                    if(angular.isUndefined(bookingCategory[channel_id])) continue;//如果没有这个预定这跳过
                    if(angular.isUndefined(bookingCategory[channel_id][item_category_id])) continue;//如果没有这个预定这跳过
                    if(angular.isUndefined(bookingCategory[channel_id][item_category_id][date_key])) continue;//如果没有这个预定这跳过
                    if(angular.isDefined(bookingCategory[channel_id][item_category_id][date_key])) {//已定房量
                        var book_num = angular.copy(bookingCategory[channel_id][item_category_id][date_key].book_num);//已定房量
                        var _room_num = angular.copy(channelRoomReservation[channel_id][item_category_id][date_key]['room_num']);
                        var room_num_reservation = _room_num - book_num;//总房量-已定房量
                        channelRoomReservation[channel_id][item_category_id][date_key]['room_num'] = room_num_reservation;
                        var max_room_num = maxChannelRoomReservation[channel_id][item_category_id].room_num;
                        if(room_num_reservation < max_room_num) maxChannelRoomReservation[channel_id][item_category_id].room_num = room_num_reservation;
                    }
                }
            }
        }
        //计算最大可定
        for(var channel_id in maxChannelRoomReservation) {
            for(var item_category_id in maxChannelRoomReservation[channel_id]) {
                var select_room_num = layoutList[channel_id][item_category_id].select_room_num;
                if(isBookRoom) {//预订单独房间
                    maxChannelRoomReservation[channel_id][item_category_id].select_room_num = select_room_num;
                } else {
                    var num = maxChannelRoomReservation[channel_id][item_category_id].room_num;
                    maxChannelRoomReservation[channel_id][item_category_id].select_room_num = [];
                    for(var j = 0; j <= num; j++) {
                        maxChannelRoomReservation[channel_id][item_category_id].select_room_num[j] = select_room_num[j];
                    }
                } 
            }
        }
        $scope.channelRoomReservation = channelRoomReservation;
        $scope.maxChannelRoomReservation = maxChannelRoomReservation;
    }
    $scope.marketChannelLayoutPrice = {};
    //选择价格体系 [根据channel_id 房型item_category_id 价格体系_system_id决定显示价格]
    var thisMarketPrice = {};
    $scope.selectMarketLayoutPrice = function() {//channel_id, item_category_id, _system_id
        //if(_system_id == '0' || typeof(_system_id) == 'undefined') return;//为0时不做任何操作
        var market_id = $scope.market_id,booking_room = $scope.booking_room;
        if(typeof(thisMarketPrice[market_id]) == 'undefined') thisMarketPrice[market_id] = {};
        var priceSystemHash = $scope.priceSystemHash;
        //var channel_id = 1,item_category_id = 1;
        for(var _system_id in priceSystemHash) {
            var priceSystem = priceSystemHash[_system_id];
            var channel_ids = priceSystem.channel_ids;
            var layout_item = priceSystem.layout_item;
            if(channel_ids == '' || layout_item == '') continue;
            channel_ids = angular.fromJson(channel_ids);
            layout_item = angular.fromJson(layout_item);
            var channel_formula = priceSystem['formula'] != '' ? angular.fromJson(priceSystem['formula']) : '';
            //if(typeof(priceSystemHash[_system_id]) != 'undefined') {
            var system_type = priceSystem['price_system_type'];
            if(system_type == 'formula') {//公式
                for(var channel_id in channel_ids) {
                    if(typeof(layout_item[channel_id]) == 'undefined') continue;
                    var decimal_price = 0;
                    if(typeof($rootScope.channelSettingList[channel_id]) != 'undefined') 
                        decimal_price = $rootScope.channelSettingList[channel_id].decimal_price - 0;
                    if(typeof(thisMarketPrice[market_id][channel_id]) == 'undefined') thisMarketPrice[market_id][channel_id] = {};
                    for(var item_category_id in layout_item[channel_id]) {
                        var key = channel_id + '-' + item_category_id;
                        var system_father_id = priceSystem['price_system_father_id'];
                        var layout_formula = channel_formula[key];
                        if(typeof(thisMarketPrice[market_id][channel_id][item_category_id]) == 'undefined') thisMarketPrice[market_id][channel_id][item_category_id] = {};
                        if(typeof(thisMarketPrice[market_id][channel_id][item_category_id][_system_id]) == 'undefined') {
                            thisMarketPrice[market_id][channel_id][item_category_id][_system_id] = {};
                        }
                        //如果价格体系在这个公司有设置
                        if(typeof(priceLayout[channel_id]) != 'undefined' && typeof(layout_formula) != 'undefined' && 
                           typeof(priceLayout[channel_id][item_category_id]) != 'undefined') {//根据公式算价格
                            //重新统计价格
                            var thisPriceLayout = priceLayout[channel_id][item_category_id][system_father_id];
                            //thisMarketPrice[market_id][channel_id][item_category_id][_system_id] = thisPriceLayout;[死循环]
                            for(var date in thisPriceLayout) {
                                var thisPrice = thisPriceLayout[date];
                                var year_month = date.substr(0,8);
                                for(var i = 1; i <= 31; i++) {
                                    var day = i < 10 ? '0'+i : i;//date_key = 年-月-日 2018-01-01
                                    var thisDayPrice = isNaN(thisPrice['day_'+day]) ? 0 : thisPrice['day_'+day];
                                    var price = thisDayPrice - 0;
                                    if(typeof(price) != 'undefined' && price > 0) {
                                        if(layout_formula['formula_value'] - 0 > 0)
                                            price = $scope.arithmetic(price, layout_formula['formula'], layout_formula['formula_value'], decimal_price);
                                        if(layout_formula['formula_second_value'] - 0 > 0)
                                            price = $scope.arithmetic(price, layout_formula['formula_second'], layout_formula['formula_second_value'], decimal_price);
                                    } else {
                                        price = '-';//未设置价格
                                    }
                                    thisMarketPrice[market_id][channel_id][item_category_id][_system_id][year_month+day] = price;
                                }
                            }
                        }
                        //清空选择的booking_room数据
                        if(angular.isDefined(booking_room[channel_id])) {
                            if(angular.isDefined(booking_room[channel_id][item_category_id])) {
                               if(angular.isDefined(booking_room[channel_id][item_category_id][_system_id])) {
                                   booking_room[channel_id][item_category_id][_system_id] = ''
                               }
                            }
                        }
                    }
                }
            } else {
                for(var channel_id in channel_ids) {
                    if(typeof(layout_item[channel_id]) == 'undefined') continue;
                    if(typeof(thisMarketPrice[market_id][channel_id]) == 'undefined') thisMarketPrice[market_id][channel_id] = {};
                    for(var item_category_id in layout_item[channel_id]) {
                        if(typeof(thisMarketPrice[market_id][channel_id][item_category_id]) == 'undefined') thisMarketPrice[market_id][channel_id][item_category_id] = {};
                        if(typeof(thisMarketPrice[market_id][channel_id][item_category_id][_system_id]) == 'undefined') {
                            thisMarketPrice[market_id][channel_id][item_category_id][_system_id] = {};
                        }
                        if(typeof(priceLayout[channel_id]) != 'undefined' && typeof(priceLayout[channel_id][item_category_id]) != 'undefined') {
                            var thisPriceLayout = priceLayout[channel_id][item_category_id][_system_id];
                            for(var date in thisPriceLayout) {
                                var thisPrice = thisPriceLayout[date];
                                var year_month = date.substr(0,8);
                                for(var i = 1; i <= 31; i++) {
                                    var day = i < 10 ? '0'+i : i;//date_key = 年-月-日 2018-01-01
                                    var price = thisPrice['day_'+day];
                                    thisMarketPrice[market_id][channel_id][item_category_id][_system_id][year_month+day] = price;
                                }
                            }
                        }
                        //清空选择的booking_room数据
                        if(angular.isDefined(booking_room[channel_id])) {
                            if(angular.isDefined(booking_room[channel_id][item_category_id])) {
                               if(angular.isDefined(booking_room[channel_id][item_category_id][_system_id])) {
                                   booking_room[channel_id][item_category_id][_system_id] = ''
                               }
                            }
                        }
                    }
                }
            }
        }
        $scope.marketChannelLayoutPrice = thisMarketPrice;//[根据channel_id 房型 价格体系决定显示价格]
        if(booking_room != '') $scope.booking_room = booking_room;
    }
    $scope.selectThisLayout = function($event) {}
    $scope.selectAllLayout = function($event) {
    };
    $scope.selectChannel = function(channel_id) {
        $scope.param["channel_father_id"] = $rootScope.employeeChannel[channel_id].channel_father_id;
    };
    //选择房间数量
    $scope.select_room = function(selectRoom,channel_id,item_category_id,price_system_id) {
        var market_id = $scope.market_id;var bookingCalendar = $scope.bookingCalendar;
        var marketChannelLayoutPrice = $scope.marketChannelLayoutPrice[market_id][channel_id][item_category_id][price_system_id];
        //找出预订有效数据
        if(angular.isUndefined($scope.booking_price[channel_id])) $scope.booking_price[channel_id] = {};
        if(angular.isUndefined($scope.booking_price[channel_id][item_category_id])) 
            $scope.booking_price[channel_id][item_category_id] = {};
        var booking_price = {};
        if(selectRoom.value > 0) {
            for(var day in bookingCalendar) {
                booking_price[day] = marketChannelLayoutPrice[day];
            }
            $scope.booking_price[channel_id][item_category_id][price_system_id] = booking_price;
        } else {//删除为0的数据 -1为删除
            $scope.booking_price[channel_id][item_category_id][price_system_id] = -1;
        }
    };
    var inputPriceChangeEvent = '';
    $scope.inputPriceChange = function(channel_id,item_category_id,price_system_id) {
        var market_id = $scope.market_id;var bookingCalendar = $scope.bookingCalendar;
        var marketChannelLayoutPrice = $scope.marketChannelLayoutPrice[market_id][channel_id][item_category_id][price_system_id];
        var booking_room = $scope.booking_room;
        if(angular.isUndefined(booking_room[channel_id])) return;
        if(angular.isUndefined(booking_room[channel_id][item_category_id])) return;
        if(angular.isUndefined(booking_room[channel_id][item_category_id][price_system_id])) return;
        var selectRoom = booking_room[channel_id][item_category_id][price_system_id];
        var booking_price = {};
        if(selectRoom.value > 0) {
            for(var day in bookingCalendar) {
                booking_price[day] = marketChannelLayoutPrice[day];
            }
            $scope.booking_price[channel_id][item_category_id][price_system_id] = booking_price;
        } else {//删除为0的数据 -1为删除
            $scope.booking_price[channel_id][item_category_id][price_system_id] = -1;
        }
        //var booking_price = $(inputPriceChangeEvent.target).value;
    }
    $scope.inputPriceChangeClick = function($event) {
        inputPriceChangeEvent = $event;
    }
    //开始预订
    $scope.beginBooking = function(search) {
        if($scope.market_father_id == '4' || search == 'member') {//判断会员是否正确
            var mobile_email = $scope.param.mobile_email, member_mobile = '', member_email = '';
            if(mobile_email.indexOf("@") != -1) {
                var emailRegexp = /^([0-9a-zA-Z\.\-\_]+)@([0-9a-zA-Z\.\-\_]+)\.([a-zA-Z]{2,5})$/i;
                if(!emailRegexp.test(mobile_email)) {
                    $alert({title: 'Error', content: '填写的Email不对！', templateUrl: '/modal-warning.html', show: true});
                    return false;
                } 
                member_email = mobile_email;
            } else {
               var mobileReg = /^(13|14|15|16|17|18|19)+\d{9}$/;
                if(!mobileReg.test(mobile_email)) {
                    $alert({title: 'Error', content: '填写的手机号不对！', templateUrl: '/modal-warning.html', show: true});
                    return false;
                } 
                member_mobile = mobile_email;
            }
            $scope.loading.start();
            $httpService.header('method', 'checkMember');
            var $checkMember = {};
            $checkMember.param = {};
            $checkMember.param['member_email']      = member_email;
            $checkMember.param['member_mobile']     = member_mobile;
            $checkMember.param['channel_father_id'] = $scope.param.channel_father_id;
            $httpService.post('/app.do?'+param, $checkMember, function(result){
                $scope.loading.percent();$httpService.deleteHeader('method');
                if(result.data.success == '0') {
                    var message = $scope.getErrorByCode(result.data.code);
                    var message_ext = '.没有找到"'+mobile_email+'"的会员记录！';
                    $alert({title: 'Notice', content: message+message_ext, templateUrl: '/modal-warning.html', show: true});
                } else {
                    $scope.market_id = result.data.item.market_id;
                    $scope.param.member_id = result.data.item.member_id;
                    if(search == 'member') {
                        //找出markey
                        var market = '';
                        for(var mi in $scope.marketList) {
                            for(var mii in $scope.marketList[mi]['children']) {
                                if($scope.market_id == $scope.marketList[mi]['children'][mii].market_id) {
                                    market = $scope.marketList[mi]['children'][mii];
                                    break;
                                }
                            }
                        }
                        if(market != '') {
                            $scope.selectCustomerMarket(market, true);
                        } else {
                            var message_ext = '没有找到"'+mobile_email+'"的会员记录！';
                            $alert({title: 'Notice', content: message_ext, templateUrl: '/modal-warning.html', show: true});
                        }
                    } else {
                        booking();
                    }
                }
            });
        } else {
            booking();
        }
        function booking() {
            $scope.beginLoading =! $scope.beginLoading;
            $scope.loading.start();
            $scope.param.market_name = $scope.market_name;
            $scope.param.market_id = $scope.market_id;
            $scope.param.market_father_id = $scope.market_father_id; 
            $scope.param['booking_data'] = {};
            var data_key = $scope.param.check_in + '|' + $scope.param.check_out;
            $scope.param['booking_data'][data_key] = {};
            $scope.param['booking_data'][data_key]['booking_room'] = $scope.booking_room;
            $scope.param['booking_data'][data_key]['booking_price'] = $scope.booking_price;
            $httpService.header('method', 'bookingRoom');
            $scope.param['id'] = $rootScope.employeeChannel[$scope.param.channel_id].id;
			if($scope.param['in_time'].length > 8) $scope.param['in_time'] = $filter('limitTo')($scope.param['in_time'], 8, 11);
			if($scope.param['out_time'].length > 8)$scope.param['out_time'] = $filter('limitTo')($scope.param['out_time'], 8, 11);
			if(angular.isDefined($scope.bookInfo) && $scope.bookInfo !== '') {
				$scope.param.booking_number = $scope.bookInfo.booking_number;
				$scope.param.book_id = $scope.bookInfo.book_id;
			}
            $httpService.post('/app.do?'+param, $scope, function(result){
                $scope.beginLoading =! $scope.beginLoading;$scope.loading.percent();
				$httpService.deleteHeader('method');
                if(result.data.success == '0') {
                    var message = $scope.getErrorByCode(result.data.code);
                    //$alert({title: 'Error', content: message, templateUrl: '/modal-warning.html', show: true});
                } else {
                    var message = $scope.getErrorByCode(result.data.code);
                    $alert({title: '预订成功', content: message, container:'body', templateUrl: '/modal-success.html', show: true});                 
					if(angular.isFunction($scope.closeAsideBookRoom)) {$scope.closeAsideBookRoom();};
                }
            })
        }
    }
    $httpService.deleteHeader('refresh');
});