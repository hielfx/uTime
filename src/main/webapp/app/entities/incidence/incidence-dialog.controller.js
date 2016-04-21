(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('IncidenceDialogController', IncidenceDialogController);

    IncidenceDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Incidence', 'Administrator', 'Request', 'AppUser'];

    function IncidenceDialogController ($scope, $stateParams, $uibModalInstance, entity, Incidence, Administrator, Request, AppUser) {
        var vm = this;
        vm.incidence = entity;
        vm.administrators = Administrator.query();
        vm.requests = Request.query();
        vm.appusers = AppUser.query();
        vm.load = function(id) {
            Incidence.get({id : id}, function(result) {
                vm.incidence = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:incidenceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.incidence.id !== null) {
                Incidence.update(vm.incidence, onSaveSuccess, onSaveError);
            } else {
                Incidence.save(vm.incidence, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.creationDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
