(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NeedDialogController', NeedDialogController);

    NeedDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Need', 'AppUser', 'NeededAbility', 'Availability', 'Request'];

    function NeedDialogController ($scope, $stateParams, $uibModalInstance, entity, Need, AppUser, NeededAbility, Availability, Request) {
        var vm = this;
        vm.need = entity;
        vm.appusers = AppUser.query();
        vm.neededabilitys = NeededAbility.query();
        vm.availabilitys = Availability.query();
        vm.requests = Request.query();
        vm.load = function(id) {
            Need.get({id : id}, function(result) {
                vm.need = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:needUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.need.id !== null) {
                Need.update(vm.need, onSaveSuccess, onSaveError);
            } else {
                Need.save(vm.need, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.modificationDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
