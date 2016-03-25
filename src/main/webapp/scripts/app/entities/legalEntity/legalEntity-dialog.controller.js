'use strict';

angular.module('volunteercrowdApp').controller('LegalEntityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LegalEntity', 'AppUser',
        function($scope, $stateParams, $uibModalInstance, $q, entity, LegalEntity, AppUser) {

        $scope.legalEntity = entity;
        $scope.appusers = AppUser.query({filter: 'legalentity-is-null'});
        $q.all([$scope.legalEntity.$promise, $scope.appusers.$promise]).then(function() {
            if (!$scope.legalEntity.appUser || !$scope.legalEntity.appUser.id) {
                return $q.reject();
            }
            return AppUser.get({id : $scope.legalEntity.appUser.id}).$promise;
        }).then(function(appUser) {
            $scope.appusers.push(appUser);
        });
        $scope.load = function(id) {
            LegalEntity.get({id : id}, function(result) {
                $scope.legalEntity = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:legalEntityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.legalEntity.id != null) {
                LegalEntity.update($scope.legalEntity, onSaveSuccess, onSaveError);
            } else {
                LegalEntity.save($scope.legalEntity, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
