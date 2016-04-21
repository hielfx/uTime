(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AbilityDialogController', AbilityDialogController);

    AbilityDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ability', 'Tag', 'AppUser'];

    function AbilityDialogController($scope, $stateParams, $uibModalInstance, entity, Ability, Tag, AppUser) {
        var vm = this;
        vm.ability = entity;
        vm.tags = Tag.query();
        vm.appusers = AppUser.query();
        vm.load = function (id) {
            Ability.get({id: id}, function (result) {
                vm.ability = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:abilityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.ability.id !== null) {
                Ability.update(vm.ability, onSaveSuccess, onSaveError);
            } else {
                Ability.save(vm.ability, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
