(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AppUserDialogController', AppUserDialogController);

    AppUserDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'AppUser', 'User', 'NaturalPerson', 'LegalEntity', 'Ability', 'Request', 'Need', 'Assessment', 'Payment', 'Incidence'];

    function AppUserDialogController ($scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, AppUser, User, NaturalPerson, LegalEntity, Ability, Request, Need, Assessment, Payment, Incidence) {
        var vm = this;
        vm.appUser = entity;
        vm.users = User.query();
        vm.appusers = AppUser.query();
        vm.naturalpersons = NaturalPerson.query();
        vm.legalentitys = LegalEntity.query();
        vm.abilitys = Ability.query();
        vm.requests = Request.query();
        vm.needs = Need.query();
        vm.assessments = Assessment.query();
        vm.payments = Payment.query();
        vm.incidences = Incidence.query();
        vm.load = function(id) {
            AppUser.get({id : id}, function(result) {
                vm.appUser = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:appUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.appUser.id !== null) {
                AppUser.update(vm.appUser, onSaveSuccess, onSaveError);
            } else {
                AppUser.save(vm.appUser, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setImage = function ($file, appUser) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        appUser.image = base64Data;
                        appUser.imageContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
