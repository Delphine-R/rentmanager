<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp"%>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Utilisateurs
            </h1>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <form class="form-horizontal" method="post" action="${pageContext.request.contextPath}/clients/update">
                            <input type="hidden" name="id" value="${client.id}">
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="nom" class="col-sm-2 control-label">Nom</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="nom" name="nom" value="${client.nom}" placeholder="Nom">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="prenom" class="col-sm-2 control-label">Prenom</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="prenom" name="prenom" value="${client.prenom}" placeholder="Prenom">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="email" class="col-sm-2 control-label">Email</label>
                                    <div class="col-sm-10">
                                        <input type="email" class="form-control" id="email" name="email" value="${client.email}" placeholder="Email">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="naissance" class="col-sm-2 control-label">Naissance</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="naissance" name="naissance" value="${naissance}" required
                                               data-inputmask="'alias': 'dd/mm/yyyy'" data-mask>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <div id="ageErrorMessage" class="text-danger"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="box-footer">
                                <button type="submit" class="btn btn-info pull-right">Modifier</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.extensions.js"></script>
<script>
    $(function () {
        $('[data-mask]').inputmask();
        function calculerAge(birthdate) {
            var age = new Date().getFullYear() - new Date(birthdate).getFullYear();
            return (new Date().getMonth() < new Date(birthdate).getMonth() || (new Date().getMonth() === new Date(birthdate).getMonth() && new Date().getDate() < new Date(birthdate).getDate())) ? age - 1 : age;
        }
        function validerNaissance() {
            var minimumAge = 18;
            var age = calculerAge($('#naissance').val());
            $('#ageErrorMessage').text(age < minimumAge ? 'Le client doit avoir minimum 18 ans' : '');
            return age >= minimumAge;
        }
        $('form').submit(function (event) {
            if (!validerNaissance()) event.preventDefault();
        });
        $('#naissance').change(validerNaissance);
    });
</script>
</body>
</html>
