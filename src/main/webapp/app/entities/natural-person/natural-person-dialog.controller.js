(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('NaturalPersonDialogController', NaturalPersonDialogController);

    NaturalPersonDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'NaturalPerson', 'Gender', 'AppUser', 'Curriculum'];

    function NaturalPersonDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, NaturalPerson, Gender, AppUser, Curriculum) {
        var vm = this;
        vm.naturalPerson = entity;
        vm.genders = Gender.query({filter: 'naturalperson-is-null'});
        $q.all([vm.naturalPerson.$promise, vm.genders.$promise]).then(function() {
            if (!vm.naturalPerson.gender || !vm.naturalPerson.gender.id) {
                return $q.reject();
            }
            return Gender.get({id : vm.naturalPerson.gender.id}).$promise;
        }).then(function(gender) {
            vm.genders.push(gender);
        });
        vm.appusers = AppUser.query({filter: 'naturalperson-is-null'});
        $q.all([vm.naturalPerson.$promise, vm.appusers.$promise]).then(function() {
            if (!vm.naturalPerson.appUser || !vm.naturalPerson.appUser.id) {
                return $q.reject();
            }
            return AppUser.get({id : vm.naturalPerson.appUser.id}).$promise;
        }).then(function(appUser) {
            vm.appusers.push(appUser);
        });
        vm.curriculums = Curriculum.query({filter: 'curriculum-is-null'});
        $q.all([vm.naturalPerson.$promise, vm.curriculums.$promise]).then(function() {
            if (!vm.naturalPerson.curriculum || !vm.naturalPerson.curriculum.id) {
                return $q.reject();
            }
            return Curriculum.get({id : vm.naturalPerson.curriculum.id}).$promise;
        }).then(function(curriculum) {
            vm.curriculums.push(curriculum);
        });
        vm.load = function(id) {
            NaturalPerson.get({id : id}, function(result) {
                vm.naturalPerson = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:naturalPersonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.naturalPerson.id !== null) {
                NaturalPerson.update(vm.naturalPerson, onSaveSuccess, onSaveError);
            } else {
                NaturalPerson.save(vm.naturalPerson, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.birthDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
