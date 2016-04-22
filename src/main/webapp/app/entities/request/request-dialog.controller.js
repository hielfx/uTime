(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('RequestDialogController', RequestDialogController);

    RequestDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Request', 'AppUser', 'Need', 'Payment', 'Incidence', 'RequestStatus'];

    function RequestDialogController ($scope, $stateParams, $uibModalInstance, entity, Request, AppUser, Need, Payment, Incidence, RequestStatus) {
        var vm = this;
        vm.request = entity;
        vm.appusers = AppUser.query();
        vm.needs = Need.query();
        vm.payments = Payment.query();
        vm.incidences = Incidence.query();
        vm.requeststatuss = RequestStatus.query();
        vm.load = function(id) {
            Request.get({id : id}, function(result) {
                vm.request = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:requestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.request.id !== null) {
                Request.update(vm.request, onSaveSuccess, onSaveError);
            } else {
                Request.save(vm.request, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.finishDate = false;
        vm.datePickerOpenStatus.modificationDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
