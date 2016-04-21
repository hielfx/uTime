(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('TagDialogController', TagDialogController);

    TagDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tag', 'Ability'];

    function TagDialogController ($scope, $stateParams, $uibModalInstance, entity, Tag, Ability) {
        var vm = this;
        vm.tag = entity;
        vm.abilitys = Ability.query();
        vm.load = function(id) {
            Tag.get({id : id}, function(result) {
                vm.tag = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:tagUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.tag.id !== null) {
                Tag.update(vm.tag, onSaveSuccess, onSaveError);
            } else {
                Tag.save(vm.tag, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
