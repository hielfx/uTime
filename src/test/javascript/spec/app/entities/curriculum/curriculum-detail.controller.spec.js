'use strict';

describe('Controller Tests', function() {

    describe('Curriculum Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCurriculum, MockNaturalPerson;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCurriculum = jasmine.createSpy('MockCurriculum');
            MockNaturalPerson = jasmine.createSpy('MockNaturalPerson');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Curriculum': MockCurriculum,
                'NaturalPerson': MockNaturalPerson
            };
            createController = function() {
                $injector.get('$controller')("CurriculumDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:curriculumUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
