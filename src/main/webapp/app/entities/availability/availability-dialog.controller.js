(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AvailabilityDialogController', AvailabilityDialogController);

    AvailabilityDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Availability', 'Need'];

    function AvailabilityDialogController ($scope, $stateParams, $uibModalInstance, entity, Availability, Need) {
        var vm = this;
        vm.availability = entity;
        vm.needs = Need.query();
        vm.load = function(id) {
            Availability.get({id : id}, function(result) {
                vm.availability = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:availabilityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.availability.id !== null) {
                Availability.update(vm.availability, onSaveSuccess, onSaveError);
            } else {
                Availability.save(vm.availability, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.startMoment = false;
        vm.datePickerOpenStatus.endMoment = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
