<!--main content-->
<script type="text/ng-template" id="/app/layout.html">
<!-- header -->
<header id="header" class="app-header navbar {{app.settings.headerColor}}" role="menu" ng-include="'/app/header/header.html'"></header>
<!-- / header -->
<!-- aside -->
<aside id="aside" class="app-aside hidden-xs {{app.settings.asideColor}}" ng-class="{'show animated fadeInLeft' : app.asideCollapse}" ng-include="'/app/header/aside.nav.menu.html'"></aside>
<!-- / aside -->
<!-- content -->
<div id="content" class="app-content" role="main" ui-view></div>
<div ui-view="header_menu"></div>
<!-- / content -->
<!-- footer -->
<footer id="footer" class="app-footer col-sm-3" role="footer" ng-include="'/app/footer.html'"></footer>
<!-- / footer -->
</script><!--main content--><!-- /app/layout.html -->
<!--common langs-->
<script type="text/ng-template" id="/langs.html">
<ul class="dropdown-menu">
	<li ng-repeat="(langKey, label) in langs">
	  <a ng-click="setLang(langKey, $event)" href>{{label}}</a>
	</li>
</ul>
</script><!-- /langs.html -->
<script type="text/ng-template" id="/login.html">
<div class="container">  
  <div class="center-block w-xl w-auto-xs m-b-lg">
    <div class="text-2x m-v-lg text-primary"><i class="fas fa-frog b-danger text-xl"></i> {{app.name}}</div>
    <div class="m-b text-sm" translate="login.hint.WORD">Sign in with your Account</div>
    <form name="login_form" class="form-validation" ng-submit="login(login_form.$valid)" ng-controller="LoginController">
      <div class="form-group m-b-xs">
        <label class="hide">Email</label>
		<!--ng-pattern="/[a-zA-Z\.\-_]@[a-zA-Z\.\-_]\.[a-z]{2,7}/"-->
        <input type="text" id="email" name="email" placeholder="手机号码/some@email.com" ng-model="param.email" required class="form-control" ng-pattern="/(([a-zA-Z\.\-_]+)@([a-zA-Z\.\-_]+)\.[a-z]{2,7})|(1[0-9]{10})/">
      </div>
      <div class="form-group m-b-xs">
        <label class="hide">Password</label>
        <input type="password" placeholder="Password" class="form-control" ng-model="param.password" required>
      </div>
      <!--<div class="checkbox no-margin"><label class="ui-checks"><input type="checkbox"><i></i> Keep me signed in</label></div>-->
      <button type="submit" class="btn btn-info p-h-md m-v-lg" btn-loading-text="{{'common.hint.LOADING' | translate}}" trigger-loading="beginLoading" translate="login.hint.SIGN">Sign in</button>
      <div class="btn-group m-b">
        <div class="btn-group dropdown">
          <button type="button" class="btn btn-link" data-animation="am-fade-and-slide-top" bs-dropdown="dropdown" aria-haspopup="true" aria-expanded="false" data-template-url="/langs.html">{{select_lang}} <span class="caret"></span>
          </button>
        </div>
      </div>
    </form>
  </div>
</div>
</script><!-- /login.html -->
<script type="text/ng-template" id="/modal-warning.html">
<div class="modal" style="z-index: 104000;" tabindex="-1" role="dialog" id="modal-warning">
  <div class="modal-dialog">
	<div class="modal-content">
	  <div class="modal-header" ng-show="title">
		<button type="button" class="close" ng-click="$hide()">&times;</button>
		<h4 class="modal-title"><i class="glyphicon glyphicon-exclamation-sign b-warning text-warning"></i> <span ng-bind-html="title"></span></h4>
	  </div>
	  <div class="modal-body" ng-show="content">
		<p ng-bind-html="content"></p>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" ng-click="$hide()">Close</button>
	  </div>
	</div>
  </div>
</div>
</script><!-- /modal-warning.html -->
<script type="text/ng-template" id="/modal-notice.html">
<div class="modal" tabindex="-1" role="dialog" id="modal-warning">
  <div class="modal-dialog">
	<div class="modal-content">
	  <div class="modal-header" ng-show="title">
		<button type="button" class="close" ng-click="$hide()">&times;</button>
		<h4 class="modal-title"><i class="glyphicon glyphicon-exclamation-sign b-warning text-warning"></i> <span ng-bind-html="title"></span></h4>
	  </div>
	  <div class="modal-body" ng-show="content">
		<p ng-bind-html="content"></p>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" ng-click="$hide()">Close</button>
	  </div>
	</div>
  </div>
</div>
</script><!-- /modal-notice.html -->
<script type="text/ng-template" id="/modal-success.html">
<div class="modal" tabindex="-1" role="dialog" id="modal-success">
  <div class="modal-dialog">
	<div class="modal-content">
	  <div class="modal-header" ng-show="title">
		<button type="button" class="close" ng-click="$hide()">&times;</button>
		<h4 class="modal-title"><i class="glyphicon glyphicon-exclamation-sign b-success text-success"></i> <span ng-bind-html="title"></span></h4>
	  </div>
	  <div class="modal-body" ng-show="content">
		<p ng-bind-html="content"></p>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" ng-click="$hide()">Close</button>
	  </div>
	</div>
  </div>
</div>
</script><!-- /modal-success.html -->
<script type="text/ng-template" id="/app/aside.html">
<div class="app-aside-inner" bs-affix>
  <div class="app-aside-body scrollable hover" ui-view="header_menu"></div>
  <div class="app-aside-footer p text-xs text-center">
    <span><strong class="text-lt">3.5</strong>GB</span>
    <div class="progress progress-xs bg no-margin m-h-xs inline w-xs">
      <div class="progress-bar progress-bar-info" style="width:30%"></div>
    </div>
    <span>10GB</span>
  </div>
</div>
</script><!-- /app/aside.html -->
<script type="text/ng-template" id="/app/footer.html">
<div class="p bg-white text-xs">
  <div class="pull-right hidden-xs hidden-sm text-muted">
    <strong>{{app.name}}</strong> -  &copy; Copyright 2019
  </div>
  <ul class="list-inline no-margin text-center-xs">
    <li>
      <a bs-dropdown="dropdown" data-placement="top-left" data-animation="am-flip-x" data-template-url="/langs.html">{{select_lang}}</a>
    </li>
    <li class="text-muted">-</li>
  </ul>
</div>
</script><!-- /app/footer.html -->
<script type="text/ng-template" id="/loading.html">
<div class="modal" tabindex="-1" role="dialog">
<div class="modal-dialog">
<div class="progress progress-striped active">
	<div class="progress-bar progress-bar-info" ng-style="{width: vm.isLoad + '%'}">Loading... {{vm.isLoad}}%</div>
</div>
</div>
</div>
</script><!-- /loading.html -->
<script type="text/ng-template" id="/app/accountsMenu.html">
    <ul class="dropdown-menu">
      <li><a href ng-click="bookingAccounts('', '')"><i class="fas fa-coins"></i> 收款</a></li>
      <li><a href ng-click="bookingAccounts('', 'refund')"><i class="fas fa-coins"></i> 退款</a></li>
      <li class="divider"></li>
      <li><a href ng-click="bookingAccounts('', 'hanging')"><i class="fas fa-comments-dollar"></i> 挂账</a></li>
    </ul>
</script><!-- /app/accountsMenu.html -->
<script type="text/ng-template" id="/app/customerMarket.html">
<aside class="app-aside bg-white lt">
<nav>
  <ul class="nav">
    <li>
        <a class="btn btn-default customer_btn" style="padding:6px !important;" id="customer_ul" ng-mouseover="showCustomerMarket($event)">
            <span class="pull-right text-muted">
              <i class="fa fa-caret-down"></i>
            </span>
            <i class="icon fa fa-users text-lt"></i>
            <span>{{market_name}}</span>
         </a>
         <ul class="nav nav-sub bg b">
            <li ng-repeat="(i, father) in marketList">
              <a class="">
                <span class="pull-right text-muted" ng-if="father.children!=''">
                  <i class="fa fa-caret-down"></i>
                </span>
                <span class="font-normal">{{father.market_name}}</span>
              </a>
              <ul class="nav nav-sub bg b" ng-if="father.children!=''">
                <li ng-repeat="(j, market) in father.children"><a ng-click="selectCustomerMarket(market, true)">{{market.market_name}}</a></li>
              </ul>
            </li>
         </ul>
    </li>
  </ul>
</nav>
</aside>
</script><!-- /app/customerMarket.html -->
<script type="text/ng-template" id="pagination.custom.html">
<nav ng-if="pages.length >= 2">
  <ul class="pagination">
    <li><a ng-click="selectPage(1)">First</a></li>
    <li><a ng-click="selectPage(currentPage - 1)">&lt;</a></li>
    <li class=""><a><page-select></page-select> of {{numPages}}</a></li>
    <li><a ng-click="selectPage(currentPage + 1)">&gt;</a></li>
    <li><a ng-click="selectPage(numPages)">Last</a></li>
  </ul>
</nav>
</script><!-- pagination.custom.html -->