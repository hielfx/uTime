'use strict';

describe('Controller Tests', function() {

    describe('Assessment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAssessment, MockAppUser, MockPayment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAssessment = jasmine.createSpy('MockAssessment');
            MockAppUser = jasmine.createSpy('MockAppUser');
            MockPayment = jasmine.createSpy('MockPayment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Assessment': MockAssessment,
                'AppUser': MockAppUser,
                'Payment': MockPayment
            };
            createController = function() {
                $injector.get('$controller')("AssessmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'volunteercrowdApp:assessmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
