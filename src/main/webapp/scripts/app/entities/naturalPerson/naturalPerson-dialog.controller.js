'use strict';

angular.module('volunteercrowdApp').controller('NaturalPersonDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'NaturalPerson', 'Gender', 'AppUser',
        function($scope, $stateParams, $uibModalInstance, $q, entity, NaturalPerson, Gender, AppUser) {

        $scope.naturalPerson = entity;
        $scope.genders = Gender.query({filter: 'naturalperson-is-null'});
        $q.all([$scope.naturalPerson.$promise, $scope.genders.$promise]).then(function() {
            if (!$scope.naturalPerson.gender || !$scope.naturalPerson.gender.id) {
                return $q.reject();
            }
            return Gender.get({id : $scope.naturalPerson.gender.id}).$promise;
        }).then(function(gender) {
            $scope.genders.push(gender);
        });
        $scope.appusers = AppUser.query({filter: 'naturalperson-is-null'});
        $q.all([$scope.naturalPerson.$promise, $scope.appusers.$promise]).then(function() {
            if (!$scope.naturalPerson.appUser || !$scope.naturalPerson.appUser.id) {
                return $q.reject();
            }
            return AppUser.get({id : $scope.naturalPerson.appUser.id}).$promise;
        }).then(function(appUser) {
            $scope.appusers.push(appUser);
        });
        $scope.load = function(id) {
            NaturalPerson.get({id : id}, function(result) {
                $scope.naturalPerson = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:naturalPersonUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.naturalPerson.id != null) {
                NaturalPerson.update($scope.naturalPerson, onSaveSuccess, onSaveError);
            } else {
                NaturalPerson.save($scope.naturalPerson, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForBirthDate = {};

        $scope.datePickerForBirthDate.status = {
            opened: false
        };

        $scope.datePickerForBirthDateOpen = function($event) {
            $scope.datePickerForBirthDate.status.opened = true;
        };
}]);
