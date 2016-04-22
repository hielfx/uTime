(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AdministratorDialogController', AdministratorDialogController);

    AdministratorDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Administrator', 'User', 'Incidence'];

    function AdministratorDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Administrator, User, Incidence) {
        var vm = this;
        vm.administrator = entity;
        vm.users = User.query();
        vm.incidences = Incidence.query();
        vm.load = function(id) {
            Administrator.get({id : id}, function(result) {
                vm.administrator = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:administratorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.administrator.id !== null) {
                Administrator.update(vm.administrator, onSaveSuccess, onSaveError);
            } else {
                Administrator.save(vm.administrator, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
