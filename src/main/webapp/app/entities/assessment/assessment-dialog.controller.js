(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AssessmentDialogController', AssessmentDialogController);

    AssessmentDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Assessment', 'AppUser', 'Payment'];

    function AssessmentDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Assessment, AppUser, Payment) {
        var vm = this;
        vm.assessment = entity;
        vm.appusers = AppUser.query();
        vm.payments = Payment.query({filter: 'assessment-is-null'});
        $q.all([vm.assessment.$promise, vm.payments.$promise]).then(function() {
            if (!vm.assessment.payment || !vm.assessment.payment.id) {
                return $q.reject();
            }
            return Payment.get({id : vm.assessment.payment.id}).$promise;
        }).then(function(payment) {
            vm.payments.push(payment);
        });
        vm.load = function(id) {
            Assessment.get({id : id}, function(result) {
                vm.assessment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('volunteercrowdApp:assessmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.assessment.id !== null) {
                Assessment.update(vm.assessment, onSaveSuccess, onSaveError);
            } else {
                Assessment.save(vm.assessment, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.creationMoment = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
