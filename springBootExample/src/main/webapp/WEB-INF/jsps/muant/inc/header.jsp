<script type="text/ng-template" id="/app/header/header.html">
<!-- navbar header -->
<div class="navbar-header" ng-class="{'box-shadow-inset dk':app.settings.headerShadow}">
	<button class="pull-right visible-xs" ng-click="app.navbarCollapse = !app.navbarCollapse"><i class="ti-settings"></i></button>
	<button class="pull-right visible-xs" ng-click="app.asideCollapse = !app.asideCollapse"><i class="ti-menu"></i></button>
<!-- brand logo -->
	<a class="navbar-brand text-lt" bs-aside="aside" data-template-url="/app/header/aside.tpl.html" data-placement="top" data-animation="am-slide-top aside-open-backdrop" data-container="body">
	  <i class="pull-right ti-arrow-circle-down text-sm m-v-xs m-l-xs"></i>
	  <i class="fas fa-frog text-xl"></i>
	  <img src="${__RESOURCE}images/logo.png" alt="." class="hide">
	  <span class="hidden-folded m-l-xs">{{app.name}}<sup class="text-xs font-thin">{{app.version}}</sup></span>
	</a>
<!-- / brand logo -->
</div>
<!-- / navbar header -->
<!-- navbar collapse -->
<div class="navbar-collapse hidden-xs" ng-class="{'show animated fadeIn' : app.navbarCollapse}">
<!-- nav -->
	<ul class="nav navbar-nav navbar-left">
	  <li ui-sref-active="active" ng-class="">
		<a bs-dropdown="dropdown" data-animation="am-collapse" data-template-url="/app/header/employeeChannel.html">
			<span class="text-muted"><i class="ui-icon fa fa-home"></i></span>
			<span ng-repeat="(i, channel) in employeeChannel" ng-if="channel.default==1">{{channel.channel_name}}</span>
			<b class="caret m-h-xs hidden-sm"></b>
		</a>
	  </li>
	  <li><a ui-sref="app.home" translate="header.navbar.DASHBOARD">首页</a></li>
	  <!--<li><a ui-sref="mail.inbox" translate="header.navbar.EMAIL">Email</a></li>
	  <li><a ui-sref="ui.component.arrow" translate="header.navbar.UIKITS">UI Kits</a></li>
	  <li><a ui-sref="page.profile" translate="header.navbar.PAGES">Pages</a></li>-->
	</ul>
<!-- / nav -->
<!-- nabar right -->
	<ul class="nav navbar-nav navbar-right m-r-n">
	  <li>
		<a href bs-dropdown="dropdown" data-animation="am-collapse" data-template-url="/app/header/settings.html">
		  <i class="glyphicon glyphicon-cog"></i>
		  <span class="visible-xs-inline">Settings</span>
		</a>
	  </li>
	  <!--<li>
		<a href bs-aside="aside" data-template-url="/app/header/aside.tpl.chat.html" data-placement="right" data-animation="am-slide-right aside-open-inner modal-open" data-container="body">
		  <i class="glyphicon glyphicon-comment"></i>
		  <b class="badge badge-xs bg-warning up">3</b>
		  <span class="visible-xs-inline">Notifications</span>
		</a>
	  </li>-->
	  <li class="hidden-xs">
		<a href ui-fullscreen><i class="glyphicon glyphicon-resize-vertical"></i></a>
	  </li>
	  <li>            
		<a href class="clear no-padding-h" bs-dropdown="dropdown" data-animation="am-collapse" data-template-url="/app/header/user.html">
		  <img ng-src="${__RESOURCE}{{employeeInfo.photo}}" onError="this.src='${__IMGWEB}userimg/user_h.png'" alt="{{employeeInfo.employee_name}}" class="navbar-img pull-right">
		  <span class="hidden-sm m-l">{{employeeInfo.employee_name}}</span>
		  <b class="caret m-h-xs hidden-sm"></b>
		</a>
	  </li>
	</ul>
<!-- / navbar right -->
<!-- search form -->
	<form class="navbar-form navbar-form-sm navbar-right visible-xs" role="search">
	  <div class="form-group">
		<div class="input-group">
		  <span class="input-group-btn">
			<button type="submit" class="btn btn-sm no-bg btn-icon no-shadow no-padder"><i class="ti-search"></i></button>
		  </span>
		  <input type="text" ng-model="selected" typeahead="state for state in states | filter:$viewValue | limitTo:8" class="form-control input-sm no-bg no-border" placeholder="Search...">              
		</div>
	  </div>
	</form>
<!-- / search form -->
</div>
<!-- / navbar collapse -->
</script><!-- /app/header/header.html -->
<script type="text/ng-template" id="/app/header/aside.nav.menu.html">
<nav ui-nav>
  <ul class="nav">
    <li class="nav-header h4 m-v-sm">
      UI Kits
    </li>
	<li ng-repeat="(i, module) in menus" ui-sref-active="active" ng-class="" class="{{module.module_channel}} {{module.hide}} menu">
		<a ng-if="module.have_children==1">
          <i class="icon {{module.ico}} text-lt"></i>
          <span>{{module.module_name}}</span>
          <span class="text-muted" ng-if="module.have_children==1">
           <i class="fa fa-caret-down"></i>
          </span>
		</a>
        <a ng-if="module.have_children==0" ui-sref="app.{{module.module_channel}}({view:module.module_view,channel:module.url})" ng-click="setActionNavName(module.module_id)">
          <i class="icon {{module.ico}} text-lt"></i>
          <span>{{module.module_name}}</span>
          <span class="pull-right text-muted" ng-if="module.have_children==1">
           <i class="fa fa-caret-down"></i>
          </span>
		</a>
		<ul class="nav nav-sub bg" ng-if="module.have_children==1">
			<li ng-repeat="(children_id, children) in module.children">
			  <a ui-sref="app.{{children.module_channel}}({view:children.module_view,channel:children.url})" ng-click="setActionNavName(children.module_id)">
				<span class="font-normal">{{children.module_name}}</span>
				<span class="pull-right text-muted" ng-if="children.have_children==1">
				  <i class="fa fa-caret-down"></i>
				</span>
			  </a>
			  <ul class="nav nav-sub bg" ng-if="children.have_children==1">
				<li ng-repeat="(submenu_id, submenu) in children.submenu">
				  <a ui-sref="app.{{submenu.module_channel}}({view:submenu.module_view,channel:submenu.url})" ng-click="setActionNavName(submenu.module_id)">{{submenu.module_name}}</a>
				</li>
			  </ul>
			</li>        
        </ul>
	</li>
  </ul>
</nav>
</script><!-- /app/header/aside.nav.menu.html -->
<script type="text/ng-template" id="/app/header/aside.tpl.html">
<div class="aside bg-dark box-shadow-lg" role="menu">
  <div class="p-lg" ng-click="$hide()">
    <a ng-repeat="(i, channel) in channels" ng-click="setChannelMenu(channel.module_channel)" class="p-lg bg-info item inline m-xs">
      <i class="{{channel.ico}} text-2x"></i>
      <span class="p-xs p-h-sm bottom text-xs hidden-xs text-center">{{channel.module_name}}</span>
    </a>
  </div>
</div>
</script><!-- /app/header/aside.tpl.html -->
<script type="text/ng-template" id="/app/header/settings.html">
<div tabindex="-1" class="p dropdown-menu bg-white bg-inherit no-b-t no-margin w-sm" role="menu">                
	<p class="clearfix">
	  <a class="inline {{color}} p-xs m-h-xs m-v-xs pull-left no-borders" ng-repeat="color in options.headerColor" ng-click="setHeaderColor(color)">
	  </a>
	</p>
	<div class="line b-b b-light m-h-xs"></div>
	<p class="clearfix">
	  <a class="inline {{color}} p-xs m-h-xs m-v-xs pull-left no-borders" ng-repeat="color in options.asideColor" ng-click="setAsideColor(color)">
	  </a>
	</p>
	<div class="line b-b b-light m-h-xs"></div>                
	<div class="m-v-xs">
	  <label class="ui-checks m-h-xs no-margin">
		<input type="checkbox" ng-model="app.settings.headerFixed"><i></i> Header fixed
	  </label>
	</div>
	<div class="m-v-xs">
	  <label class="ui-checks m-h-xs no-margin">
		<input type="checkbox" ng-model="app.settings.headerShadow"><i></i> Header shadow
	  </label>
	</div>
	<div class="m-v-xs">
	  <label class="ui-checks m-h-xs no-margin">
		<input type="checkbox" ng-model="app.settings.asideTop"><i></i> Aside top
	  </label>
	</div>
</div>
</script><!-- /app/header/settings.html -->
<script type="text/ng-template" id="/app/header/aside.tpl.chat.html">
<div class="aside w b-l" role="dialog">
  <div class="box">
    <div class="p">
      <a ng-click="$hide()" class="pull-right text-muted"><i class="fa fa-times"></i></a>
      Chat
    </div>
    <div class="box-row">
      <div class="box-cell">
        <div class="box-inner">
          <div class="list-group no-radius no-borders">
            <a class="list-group-item p-h-md">
              <img src="${__RESOURCE}images/a1.jpg" class="pull-left w-thumb m-r b-b b-b-2x b-success">
              <div class="clear">
                <span class="font-bold block">Jonathan Doe</span>
                <span class="clear text-ellipsis text-xs">"Hey, What's up"</span>
              </div>
            </a>
            <a class="list-group-item p-h-md">
              <img src="${__RESOURCE}images/a2.jpg" class="pull-left w-thumb m-r b-b b-b-2x b-success">
              <div class="clear">
                <span class="font-bold block">James Pill</span>
                <span class="clear text-ellipsis text-xs">"Lorem ipsum dolor sit amet onsectetur adipiscing elit"</span>
              </div>
            </a>
            <div class="p-h-md p-v-xs m-t">Work</div>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="fa fa-circle text-success text-xs m-r-xs"></i>
                <span>Jonathan Morina</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="fa fa-circle text-success text-xs m-r-xs"></i>
                <span>Mason Yarnell</span>
            </a>
            <a class="list-group-item p-h-md p-v-xs">
                <i class="fa fa-circle text-muted-lt text-xs m-r-xs"></i>
                <span>Jeff Broderik</span>
            </a>
            <div class="p-h-md p-v-xs m-t">Partner</div>            
            <a class="list-group-item p-h-md p-v-xs">
                <i class="fa fa-circle text-success text-xs m-r-xs"></i>
                <span>Mason Yarnell</span>
            </a>
            
          </div>
        </div>
      </div>
    </div>
    <div class="p-h-md p-v">
      <p>Invite People</p>
      <a href class="text-muted"><i class="fa fa-fw fa-twitter"></i> Twitter</a>
      <a href class="text-muted"><i class="fa fa-fw fa-facebook"></i> Facebook</a>
    </div>
  </div>
</div>
</script><!-- /app/header/aside.tpl.chat.html -->
<script type="text/ng-template" id="/app/header/user.html">
<ul class="dropdown-menu pull-right no-b-t">
	<!--<li><a ui-sref="page.profile">Profile</a></li>
	<li><a ui-sref="page.settings">Settings</a></li>
	<li class="divider"></li>
	<li><a ui-sref="lockme">Lock me</a></li>-->
	<li><a ui-sref="app.logout">Logout</a></li>
</ul>
</script><!-- /app/header/user.html -->
<script type="text/ng-template" id="/app/header/employeeChannel.html">
<ul class="dropdown-menu pull-left no-b-t" ng-class="{'show animated fadeIn' : app.navbarCollapse}">
	<li ng-repeat="(i, channel) in employeeChannel" ng-click="switchChannel(channel.channel_id)" ng-if="channel.channel_father_id==channel.channel_id"><a>{{channel.channel_name}}</a></li>
</ul>
</script><!-- /app/header/employeeChannel.html -->
