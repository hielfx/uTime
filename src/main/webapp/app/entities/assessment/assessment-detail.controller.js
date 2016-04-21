(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('AssessmentDetailController', AssessmentDetailController);

    AssessmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Assessment', 'AppUser', 'Payment'];

    function AssessmentDetailController($scope, $rootScope, $stateParams, entity, Assessment, AppUser, Payment) {
        var vm = this;
        vm.assessment = entity;
        vm.load = function (id) {
            Assessment.get({id: id}, function(result) {
                vm.assessment = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:assessmentUpdate', function(event, result) {
            vm.assessment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
