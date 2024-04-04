<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<%@ include file="/WEB-INF/views/common/head.jsp" %>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-md-3">

                    <!-- Reservation Details -->
                    <div class="box box-primary">
                        <div class="box-body box-profile">
                            <h3 class="profile-username text-center">Reservation ${reservation.id}</h3>

                            <ul class="list-group list-group-unbordered">
                                <li class="list-group-item">
                                    <b>Client</b> <a class="pull-right">${client.nom} ${client.prenom}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Vehicules</b> <a class="pull-right">${nb_vehicles}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Date de debut</b> <a class="pull-right">${reservation.debut}</a>
                                </li>
                                <li class="list-group-item">
                                    <b>Date de fin</b> <a class="pull-right">${reservation.fin}</a>
                                </li>
                            </ul>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
                <div class="col-md-9">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#cars" data-toggle="tab">Vehicles</a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="active tab-pane" id="cars">
                                <!-- /.box-header -->
                                <div class="box-body no-padding">
                                    <table class="table table-striped">
                                        <tr>
                                            <th>#</th>
                                            <th>Modele</th>
                                            <th>Constructeur</th>
                                        </tr>
                                        <c:forEach var="vehicle" items="${vehicles}">
                                            <tr>
                                                <td>${vehicle.id}</td>
                                                <td>${vehicle.modele}</td>
                                                <td>${vehicle.constructeur}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                            <!-- /.tab-pane -->
                        </div>
                        <!-- /.tab-content -->
                    </div>
                    <!-- /.nav-tabs-custom -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->

        </section>
        <!-- /.content -->
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
<!-- ./wrapper -->

<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
</body>
</html>
