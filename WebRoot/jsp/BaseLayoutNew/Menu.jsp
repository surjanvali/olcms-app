<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<style>
.page-sidebar li {
	margin-top: 0px;
	margin-bottom: 0px;
	border: 0;
	border-top: 1px solid rgb(55, 79, 101);
}
</style>

<nav class="page-sidebar" id="sidebar">
	<div id="sidebar-collapse">
		<!-- <div class="admin-block d-flex"> -->
		<div class="admin-block ">
			<div>
				<img src="./assetsnew/img/admin-avatar.png"
					style="display:block; width:45px; margin:0px auto;" />
			</div>
			<div class="admin-info"
				style="text-align:center; padding-left:0px;padding-top:10px; ">
				<!-- <div class="font-strong">James Brown</div>
				<small>Administrator</small> -->
				<c:if test="${sessionScope.role_id eq 2}">

					<div class="font-strong">District Collector</div>
					<small>${sessionScope.userName }</small>
				</c:if>
				<c:if
					test="${sessionScope.role_id eq 3 || sessionScope.role_id eq 4 || sessionScope.role_id eq 5 || sessionScope.role_id eq 8  
				|| sessionScope.role_id eq 9 || sessionScope.role_id eq 10 || sessionScope.role_id eq 11 || sessionScope.role_id eq 12  || sessionScope.role_id eq 13 || sessionScope.role_id eq 14|| sessionScope.role_id eq 15}">

					<div class="font-strong">${sessionScope.userName }</div>
					<small>${sessionScope.role_desc }</small>
					<br />
					<small>${sessionScope.dept_desc }</small>

				</c:if>
				<c:if test="${sessionScope.role_id eq 6}">
					<!-- <div class="font-strong">GP Office</div> -->
					<div class="font-strong">${sessionScope.userName }</div>
					<small>${sessionScope.role_desc }</small>
					<br />
					<small>${sessionScope.dept_desc }</small>

				</c:if>
				<c:if test="${sessionScope.role_id eq 7 }">
					<div class="font-strong">${sessionScope.userName }</div>
				</c:if>
				<%-- <c:if test="${sessionScope.role_id eq 8 }">
					<div class="font-strong">${sessionScope.userName }</div>

				</c:if> --%>
				<logic:notEmpty name="lastLogin" scope="session">
					<br />
					<small>Last Login : ${sessionScope.lastLogin }</small>
				</logic:notEmpty>
			</div>
		</div>
		<!-- <hr
			style="margin-top: 0px; margin-bottom: 0px;border: 0;border-top: 1px solid rgb(55 79 101);"> -->

		<ul class="side-menu metismenu" style="padding-bottom: 50px;">
			<!-- <li><a class="active" href="index.html"><i
					class="sidebar-item-icon fa fa-th-large"></i> <span
					class="nav-label">Dashboard</span> </a></li>
			<li class="heading">FEATURES</li>

			<li><a href="javascript:;"><i
					class="sidebar-item-icon fa fa-edit"></i> <span class="nav-label">Forms</span><i
					class="fa fa-angle-left arrow"></i> </a>
				<ul class="nav-2-level collapse">
					<li><a href="form_basic.html">Basic Forms</a></li>
					<li><a href="form_advanced.html">Advanced Plugins</a></li>
					<li><a href="form_masks.html">Form input masks</a></li>
					<li><a href="form_validation.html">Form Validation</a></li>
					<li><a href="text_editors.html">Text Editors</a></li>
				</ul></li> -->

			<logic:notEmpty name='services' scope="session">
				<logic:iterate id="service" name="services" scope="session">
					<c:if test="${service.parent_id eq 0 && service.has_child eq true}">
						<!-- fa fa-edit -->
						<li><a href="javascript:;"><i
								class="sidebar-item-icon ${service.icon}"></i> <span
								class="nav-label">${service.service_name}</span><i
								class="fa fa-angle-left arrow"></i> </a>

							<ul class="nav-2-level collapse">
								<logic:iterate id="inner_service" name="services"
									scope="session">
									<c:if test="${service.service_id eq inner_service.parent_id }">
										<li><a href="${inner_service.target }"><i
												class="sidebar-item-icon ${inner_service.icon }"></i>
												${inner_service.service_name}</a></li>
									</c:if>
								</logic:iterate>
							</ul></li>
					</c:if>
					<c:if
						test="${service.parent_id eq 0 && service.has_child eq false}">

						<c:if test="${service.target ne 'Logout.do'}">
							<!-- fa fa-bookmark -->
							<li><a href="${service.target }"><i
									class="sidebar-item-icon ${service.icon}"></i> <span
									class="nav-label">${service.service_name}</span> </a></li>
						</c:if>

						<c:if test="${service.target eq 'Logout.do'}">
							<!-- fa fa-power-off -->
							<li><a href="${service.target }"><i
									class="sidebar-item-icon ${service.icon}"></i> <span
									class="nav-label">${service.service_name}</span> </a></li>
						</c:if>

					</c:if>
				</logic:iterate>
			</logic:notEmpty>
			<%-- <logic:notEmpty name="user_manuals" scope="session">
				<li class="heading">User Manuals</li>
				<logic:iterate id="manual" name="user_manuals" scope="session">
					<li class="nav-2-level collapse"><a href="${manual.usermanual_path }" target="_blank">${manual.description }</a></li>
				</logic:iterate>
			</logic:notEmpty> --%>

		</ul>


		<logic:notEmpty name="user_manuals" scope="session">
			<div class="row">
				<div class="col-md-12">
					<ul class="side-menu metismenu">
						<li
							style="color: #fff;font-weight: bold;text-align: center;padding: 3px;"><i
							class="sidebar-item-icon fa fa-download"></i><span
							class="nav-label">Download User Manuals</span></li>
						<logic:iterate id="manual" name="user_manuals" scope="session">
							<li><a href="${manual.usermanual_path }" target="_blank"
								style="word-wrap: break-word;">${manual.description }</a></li>
						</logic:iterate>
					</ul>
				</div>

			</div>
		</logic:notEmpty>

	</div>
</nav>