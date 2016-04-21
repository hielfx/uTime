(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('LegalEntityDialogController', LegalEntityDialogController);

    LegalEntityDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LegalEntity', 'AppUser'];

    function LegalEntityDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, LegalEntity, AppUser) {
        var vm = this;
        vm.legalEntity = entity;
        vm.appusers = AppUser.query({filter: 'legalentity-is-null'});
        $q.all([vm.legalEntity.$promise, vm.appusers.$promise]).then(function() {
            if (!vm.legalEntity.appUser || !vm.legalEntity.appUser.id) {
                return $q.reject();
            }
            return AppUser.get({id : vm.legalEntity.appUser.id}).$promise;
        }).then(function(appUser) {
            vm.appusers.push(appUser);
        });
        vm.load = function(id) {
            LegalEntity.get({id : id}, function(result) {
                vm.legalEntity = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:legalEntityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.legalEntity.id !== null) {
                LegalEntity.update(vm.legalEntity, onSaveSuccess, onSaveError);
            } else {
                LegalEntity.save(vm.legalEntity, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
