(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NeededAbilityDialogController', NeededAbilityDialogController);

    NeededAbilityDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'NeededAbility', 'Need'];

    function NeededAbilityDialogController ($scope, $stateParams, $uibModalInstance, entity, NeededAbility, Need) {
        var vm = this;
        vm.neededAbility = entity;
        vm.needs = Need.query();
        vm.load = function(id) {
            NeededAbility.get({id : id}, function(result) {
                vm.neededAbility = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:neededAbilityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.neededAbility.id !== null) {
                NeededAbility.update(vm.neededAbility, onSaveSuccess, onSaveError);
            } else {
                NeededAbility.save(vm.neededAbility, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
