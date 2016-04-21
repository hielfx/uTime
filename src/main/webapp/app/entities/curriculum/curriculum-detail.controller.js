(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .controller('CurriculumDetailController', CurriculumDetailController);

    CurriculumDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Curriculum', 'NaturalPerson'];

    function CurriculumDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Curriculum, NaturalPerson) {
        var vm = this;
        vm.curriculum = entity;
        vm.load = function (id) {
            Curriculum.get({id: id}, function(result) {
                vm.curriculum = result;
            });
        };
        var unsubscribe = $rootScope.$on('volunteercrowdApp:curriculumUpdate', function(event, result) {
            vm.curriculum = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
